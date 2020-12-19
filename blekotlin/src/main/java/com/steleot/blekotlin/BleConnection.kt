package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.content.Context
import com.steleot.blekotlin.internal.BLE_GATT_MAX_MTU_SIZE
import com.steleot.blekotlin.internal.BLE_GATT_MIN_MTU_SIZE
import com.steleot.blekotlin.internal.BleDescriptors
import com.steleot.blekotlin.internal.BleOperation
import com.steleot.blekotlin.internal.CONNECT_DELAY
import com.steleot.blekotlin.internal.CharacteristicRead
import com.steleot.blekotlin.internal.CharacteristicWrite
import com.steleot.blekotlin.internal.Connect
import com.steleot.blekotlin.internal.DescriptorRead
import com.steleot.blekotlin.internal.DescriptorWrite
import com.steleot.blekotlin.internal.DisableNotifications
import com.steleot.blekotlin.internal.Disconnect
import com.steleot.blekotlin.internal.EnableNotifications
import com.steleot.blekotlin.internal.MtuRequest
import com.steleot.blekotlin.internal.utils.findCharacteristic
import com.steleot.blekotlin.internal.utils.findDescriptor
import com.steleot.blekotlin.internal.utils.getBleGattStatus
import com.steleot.blekotlin.internal.utils.getCharacteristicName
import com.steleot.blekotlin.internal.utils.getDescriptorName
import com.steleot.blekotlin.internal.utils.isClientCharacteristicConfigurationDescriptor
import com.steleot.blekotlin.internal.utils.isIndicatable
import com.steleot.blekotlin.internal.utils.isNotifiable
import com.steleot.blekotlin.internal.utils.isReadable
import com.steleot.blekotlin.internal.utils.isWritable
import com.steleot.blekotlin.internal.utils.printGattInformation
import com.steleot.blekotlin.internal.utils.toBluetoothUuid
import com.steleot.blekotlin.internal.utils.toHexString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedQueue

private const val TAG = "BleConnection"

@SuppressLint("MissingPermission")
class BleConnection(
    private val bleLogger: BleLogger
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var bleGatt: BleGatt? = null
    private val operationsQueue = ConcurrentLinkedQueue<BleOperation>()
    private var pendingOperation: BleOperation? = null

    internal fun connect(
        device: BleDevice,
        context: Context
    ) {
        if (bleGatt != null
            && device.address == bleGatt!!.device.address
        ) {
            bleLogger.log(TAG, "Already connected to device ${device.address}")
        } else {
            enqueueOperation(Connect(device, context.applicationContext))
        }
    }

    internal fun shouldReconnect() = bleGatt != null

    internal fun reconnect(
        context: Context
    ) {
        bleGatt?.let {
            enqueueOperation(Connect(it.device, context.applicationContext))
        }
    }

    internal fun teardownConnection() {
        if (bleGatt != null) {
            enqueueOperation(Disconnect(bleGatt!!.device))
        } else {
            bleLogger.log(
                TAG, "Not connected to any device. Cannot " +
                        "teardown the connection."
            )
        }
    }

    fun readCharacteristic(
        characteristicUuid: UUID
    ) {
        if (bleGatt != null) {
            val device = bleGatt!!.device
            enqueueOperation(CharacteristicRead(device, characteristicUuid))
        } else {
            bleLogger.log(TAG, "Not connected to any device, cannot read characteristic")
        }
    }

    fun writeCharacteristic(
        characteristicUuid: UUID,
        payload: ByteArray
    ) {
        if (bleGatt != null) {
            val device = bleGatt!!.device
            enqueueOperation(CharacteristicWrite(device, characteristicUuid, payload))
        } else {
            bleLogger.log(TAG, "Not connected to any device, cannot read characteristic")
        }
    }

    fun readDescriptor(
        descriptorUuid: UUID
    ) {
        if (bleGatt != null) {
            val device = bleGatt!!.device
            enqueueOperation(DescriptorRead(device, descriptorUuid))
        } else {
            bleLogger.log(
                TAG,
                "Not connected to any device, cannot perform descriptor read"
            )
        }
    }

    fun writeDescriptor(
        descriptorUuid: UUID,
        payload: ByteArray
    ) {
        if (bleGatt != null) {
            val device = bleGatt!!.device
            enqueueOperation(DescriptorWrite(device, descriptorUuid, payload))
        } else {
            bleLogger.log(
                TAG,
                "Not connected to any device, cannot write descriptor"
            )
        }
    }

    fun enableNotifications(
        characteristicUuid: UUID
    ) {
        handleNotifications(characteristicUuid)
    }

    private fun handleNotifications(
        characteristicUuid: UUID,
        isEnable: Boolean = true
    ) {
        if (bleGatt != null) {
            val device = bleGatt!!.device
            enqueueOperation(
                if (isEnable) EnableNotifications(device, characteristicUuid)
                else DisableNotifications(device, characteristicUuid)
            )
        } else {
            bleLogger.log(TAG, "Not connected to any device, cannot change notifications")
        }
    }

    fun disableNotifications(
        characteristicUuid: UUID
    ) {
        handleNotifications(characteristicUuid, false)
    }

    fun requestMtu(
        mtu: Int
    ) {
        if (bleGatt != null) {
            val device = bleGatt!!.device
            enqueueOperation(
                MtuRequest(
                    device,
                    mtu.coerceIn(BLE_GATT_MIN_MTU_SIZE, BLE_GATT_MAX_MTU_SIZE)
                )
            )
        } else {
            bleLogger.log(TAG, "Not connected to any device, cannot request MTU update!")
        }
    }

    @Synchronized
    private fun enqueueOperation(
        operation: BleOperation
    ) {
        operationsQueue.add(operation)
        if (pendingOperation == null) {
            doNextOperation()
        }
    }

    @Synchronized
    private fun signalEndOfOperation() {
        bleLogger.log(TAG, "End of $pendingOperation.")
        pendingOperation = null
        if (operationsQueue.isNotEmpty()) {
            doNextOperation()
        }
    }

    @Synchronized
    private fun doNextOperation() {
        if (pendingOperation != null) {
            bleLogger.log(TAG, "No pending operation found. Aborting operation.")
            return
        }

        val operation = operationsQueue.poll() ?: run {
            bleLogger.log(TAG, "Operations queue is empty. Aborting operation.")
            return
        }
        pendingOperation = operation

        if (operation is Connect) {
            with(operation) {
                bleLogger.log(TAG, "Attempting to connect to device ${bleDevice.address}")
                coroutineScope.launch {
                    delay(CONNECT_DELAY)
                    bleDevice.connectGatt(context, false, callback)
                }
            }
            return
        }

        if (bleGatt == null) {
            bleLogger.log(
                TAG, "Not connected to device ${operation.bleDevice.address}." +
                        "Aborting operation."
            )
            signalEndOfOperation()
            return
        }

        val gatt = bleGatt!!

        when (operation) {
            is Disconnect -> with(operation) {
                bleLogger.log(TAG, "Disconnecting from device ${bleDevice.address}")
                gatt.close()
                bleGatt = null
                signalEndOfOperation()
            }
            is CharacteristicWrite -> with(operation) {
                findCharacteristic(gatt, characteristicUuid) { characteristic ->
                    // todo validations && write type
                    characteristic.writeType = BleGattCharacteristic.WRITE_TYPE_DEFAULT
                    characteristic.value = payload
                    gatt.writeCharacteristic(characteristic)
                }
            }
            is CharacteristicRead -> with(operation) {
                findCharacteristic(gatt, characteristicUuid) { characteristic ->
                    if (characteristic.isReadable()) gatt.readCharacteristic(characteristic)
                    else {
                        bleLogger.log(
                            TAG, characteristic.uuid.getCharacteristicName() +
                                    " isn't readable."
                        )
                        signalEndOfOperation()
                    }
                }
            }
            is DescriptorWrite -> with(operation) {
                findDescriptor(gatt, descriptorUuid) { descriptor ->
                    if (descriptor.isWritable()
                        || descriptor.isClientCharacteristicConfigurationDescriptor()
                    ) {
                        gatt.writeDescriptor(descriptor)
                    } else {
                        bleLogger.log(
                            TAG, descriptor.uuid.getCharacteristicName() +
                                    " isn't writable."
                        )
                    }
                }
            }
            is DescriptorRead -> with(operation) {
                findDescriptor(gatt, descriptorUuid) { descriptor ->
                    if (descriptor.isReadable()) {
                        gatt.readDescriptor(descriptor)
                    } else {
                        bleLogger.log(
                            TAG, descriptor.uuid.getDescriptorName() +
                                    " isn't readable."
                        )
                        signalEndOfOperation()
                    }
                }
            }
            is EnableNotifications -> with(operation) {
                findCharacteristic(gatt, characteristicUuid) { characteristic ->
                    // todo validations
                    val uuid = BleDescriptors.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuid()
                    val payload = when {
                        characteristic.isIndicatable() ->
                            BleGattDescriptor.ENABLE_INDICATION_VALUE
                        characteristic.isNotifiable() ->
                            BleGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        else ->
                            error(
                                "${characteristic.uuid.getCharacteristicName()} doesn't " +
                                        "support notifications / indications"
                            )
                    }
                    characteristic.getDescriptor(uuid)?.let { descriptor ->
                        if (!gatt.setCharacteristicNotification(characteristic, true)) {
                            bleLogger.log(
                                TAG, "Setting notification failed for " +
                                        characteristic.uuid.getDescriptorName()
                            )
                            signalEndOfOperation()
                            return@findCharacteristic
                        }

                        descriptor.value = payload
                        gatt.writeDescriptor(descriptor)
                    } ?: run {
                        bleLogger.log(
                            TAG, "Cannot find " +
                                    "${characteristicUuid.getCharacteristicName()}. Failed to enable" +
                                    " notifications."
                        )
                        signalEndOfOperation()
                    }
                }
            }
            is DisableNotifications -> with(operation) {
                findCharacteristic(gatt, characteristicUuid) { characteristic ->
                    // todo validations
                    val uuid = BleDescriptors.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuid()
                    characteristic.getDescriptor(uuid)?.let { cccDescriptor ->
                        if (!gatt.setCharacteristicNotification(characteristic, false)) {
                            bleLogger.log(
                                TAG, "Setting notification failed for " +
                                        characteristic.uuid.getDescriptorName()
                            )
                            signalEndOfOperation()
                            return@findCharacteristic
                        }

                        cccDescriptor.value = BleGattDescriptor.DISABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(cccDescriptor)
                    } ?: run {
                        bleLogger.log(
                            TAG, "Cannot find " +
                                    "${characteristicUuid.getCharacteristicName()}. Failed to enable" +
                                    " notifications."
                        )
                        signalEndOfOperation()
                    }
                }
            }
            is MtuRequest -> with(operation) {
                gatt.requestMtu(mtu)
            }
            else -> signalEndOfOperation()
        }
    }

    private fun findCharacteristic(
        bleGatt: BleGatt,
        characteristicUuid: UUID,
        successBlock: (BleGattCharacteristic) -> Unit
    ) {
        bleGatt.findCharacteristic(characteristicUuid)?.let {
            successBlock(it)
        } ?: run {
            bleLogger.log(
                TAG, "Cannot find characteristic " +
                        "${characteristicUuid.getCharacteristicName()}. Ending operation.."
            )
            signalEndOfOperation()
        }
    }

    private fun findDescriptor(
        bleGatt: BleGatt,
        descriptorUuid: UUID,
        successBlock: (BleGattDescriptor) -> Unit
    ) {
        bleGatt.findDescriptor(descriptorUuid)?.let {
            successBlock(it)
        } ?: run {
            bleLogger.log(
                TAG, "Cannot find descriptor " +
                        "${descriptorUuid.getDescriptorName()}. Ending operation.."
            )
            signalEndOfOperation()
        }
    }

    private val callback = object : BleGattCallback() {

        override fun onConnectionStateChange(
            bleGatt: BleGatt,
            status: Int,
            newState: Int
        ) {
            val deviceAddress = bleGatt.device.address
            when (status) {
                BleGatt.GATT_SUCCESS -> {
                    if (newState == BleProfile.STATE_CONNECTED) {
                        bleLogger.log(TAG, "Successfully connected to $deviceAddress")
                        this@BleConnection.bleGatt = bleGatt
                        coroutineScope.launch { bleGatt.discoverServices() }
                    } else if (newState == BleProfile.STATE_DISCONNECTED) {
                        bleLogger.log(TAG, "Successfully disconnected from $deviceAddress")
                        teardownConnection()
                    }
                }
                BleGatt.GATT_INSUFFICIENT_ENCRYPTION,
                BleGatt.GATT_INSUFFICIENT_AUTHENTICATION -> {
                    bleLogger.log(TAG, "Trying to bond with $deviceAddress")
                    bleGatt.device.createBond()
                }
                else -> {
                    if (pendingOperation is Connect) {
                        signalEndOfOperation()
                    }
                    teardownConnection()
                }
            }
        }

        override fun onServicesDiscovered(
            gatt: BleGatt,
            status: Int
        ) {
            with(gatt) {
                if (status == BleGatt.GATT_SUCCESS) {
                    bleLogger.log(
                        TAG, "Discovered ${services.size} services for " +
                                "device ${device.address}."
                    )
                    printGattInformation(bleLogger, TAG)
                    requestMtu(BLE_GATT_MAX_MTU_SIZE)
                } else {
                    bleLogger.log(TAG, "Service discovery failed due to status $status")
                    teardownConnection()
                }
            }

            if (pendingOperation is Connect) {
                signalEndOfOperation()
            }
        }

        override fun onMtuChanged(
            gatt: BleGatt,
            mtu: Int,
            status: Int
        ) {
            bleLogger.log(
                TAG, "Mtu changed to $mtu, success: " +
                        "${status == BleGatt.GATT_SUCCESS}"
            )
            if (pendingOperation is MtuRequest) {
                signalEndOfOperation()
            }
        }

        override fun onCharacteristicRead(
            gatt: BleGatt,
            characteristic: BleGattCharacteristic,
            status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BleGatt.GATT_SUCCESS -> {
                        bleLogger.log(
                            TAG, "Read characteristic " +
                                    "${uuid.getCharacteristicName()} | value: ${value.toHexString()}"
                        )
                    }
                    BleGatt.GATT_READ_NOT_PERMITTED -> {
                        bleLogger.log(
                            TAG, "Read not permitted " +
                                    "for ${uuid.getCharacteristicName()}."
                        )
                    }
                    else -> {
                        bleLogger.log(
                            TAG, "Characteristic read failed for " +
                                    "${uuid.getCharacteristicName()}," +
                                    "error: ${status.getBleGattStatus()}"
                        )
                    }
                }
            }

            if (pendingOperation is CharacteristicRead) {
                signalEndOfOperation()
            }
        }

        override fun onCharacteristicWrite(
            gatt: BleGatt,
            characteristic: BleGattCharacteristic,
            status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BleGatt.GATT_SUCCESS -> {
                        bleLogger.log(
                            TAG, "Wrote to characteristic " +
                                    "${uuid.getCharacteristicName()} | value: ${value.toHexString()}"
                        )
                    }
                    BleGatt.GATT_WRITE_NOT_PERMITTED -> {
                        bleLogger.log(
                            TAG, "Write not permitted " +
                                    "for ${uuid.getCharacteristicName()}."
                        )
                    }
                    else -> {
                        bleLogger.log(
                            TAG, "Characteristic write failed for " +
                                    "${uuid.getCharacteristicName()}," +
                                    "error: ${status.getBleGattStatus()}"
                        )
                    }
                }
            }

            if (pendingOperation is CharacteristicWrite) {
                signalEndOfOperation()
            }
        }

        override fun onCharacteristicChanged(
            gatt: BleGatt,
            characteristic: BleGattCharacteristic
        ) {
            with(characteristic) {
                bleLogger.log(TAG, "Characteristic $uuid changed | value: ${value.toHexString()}")
            }
        }

        override fun onDescriptorRead(
            gatt: BleGatt,
            descriptor: BleGattDescriptor,
            status: Int
        ) {
            with(descriptor) {
                when (status) {
                    BleGatt.GATT_SUCCESS -> {
                        bleLogger.log(
                            TAG, "Read descriptor " +
                                    "${uuid.getDescriptorName()} | value: ${value.toHexString()}"
                        )
                    }
                    BleGatt.GATT_READ_NOT_PERMITTED -> {
                        bleLogger.log(
                            TAG, "Read not permitted " +
                                    "for ${uuid.getDescriptorName()}."
                        )
                    }
                    else -> {
                        bleLogger.log(
                            TAG, "Descriptor read failed for " +
                                    "${uuid.getDescriptorName()}," +
                                    "error: ${status.getBleGattStatus()}"
                        )
                    }
                }
            }

            if (pendingOperation is DescriptorRead) {
                signalEndOfOperation()
            }
        }

        override fun onDescriptorWrite(
            gatt: BleGatt,
            descriptor: BleGattDescriptor,
            status: Int
        ) {
            with(descriptor) {
                when (status) {
                    BleGatt.GATT_SUCCESS -> {
                        bleLogger.log(
                            TAG, "Wrote to descriptor " +
                                    "${uuid.getDescriptorName()} | value: ${value.toHexString()}"
                        )
                        if (isClientCharacteristicConfigurationDescriptor()) {
                            onClientCharacteristicConfigurationDescriptorWrite(
                                gatt, value, characteristic
                            )
                        }
                    }
                    BleGatt.GATT_WRITE_NOT_PERMITTED -> {
                        bleLogger.log(
                            TAG, "Write not permitted " +
                                    "for ${uuid.getDescriptorName()}."
                        )
                    }
                    else -> {
                        bleLogger.log(
                            TAG, "Characteristic write failed for " +
                                    "${uuid.getDescriptorName()}, error: ${status.getBleGattStatus()}"
                        )
                    }
                }
            }

            if (descriptor.isClientCharacteristicConfigurationDescriptor() &&
                (pendingOperation is EnableNotifications
                        || pendingOperation is DisableNotifications)
            ) {
                signalEndOfOperation()
            } else if (!descriptor.isClientCharacteristicConfigurationDescriptor()
                && pendingOperation is DescriptorWrite
            ) {
                signalEndOfOperation()
            }
        }

        private fun onClientCharacteristicConfigurationDescriptorWrite(
            bleGatt: BleGatt,
            value: ByteArray,
            characteristic: BleGattCharacteristic
        ) {
            val characteristicUuid = characteristic.uuid
            val notificationsEnabled =
                value.contentEquals(BleGattDescriptor.ENABLE_NOTIFICATION_VALUE) ||
                        value.contentEquals(BleGattDescriptor.ENABLE_INDICATION_VALUE)
            val notificationsDisabled =
                value.contentEquals(BleGattDescriptor.DISABLE_NOTIFICATION_VALUE)

            when {
                notificationsEnabled -> {
                    bleLogger.log(
                        TAG, "Notifications or indications enabled on " +
                                characteristicUuid.getCharacteristicName()
                    )
                }
                notificationsDisabled -> {
                    bleLogger.log(
                        TAG, "Notifications or indications disabled on " +
                                characteristicUuid.getCharacteristicName()
                    )
                }
                else -> {
                    bleLogger.log(
                        TAG, "Unexpected value ${value.toHexString()} on " +
                                "Client Characteristic Configuration Descriptor of " +
                                characteristicUuid.getCharacteristicName()
                    )
                }
            }
        }
    }
}
