@file:Suppress("unused")

package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.content.Context
import com.steleot.blekotlin.constants.BleGattDescriptorUuids
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.*
import com.steleot.blekotlin.internal.utils.*
import com.steleot.blekotlin.status.BleConnectionStatus
import com.steleot.blekotlin.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

private const val TAG = "BleConnection"

/**
 * The core class of doing gatt ble actions with a connected device. All actions are added in
 * queue and are executed in sequence.
 */
@SuppressLint("MissingPermission") // private constructor
class BleConnection internal constructor(
        private val bleLogger: BleLogger
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val deviceGattMap = ConcurrentHashMap<BleDevice, BleGatt>()
    private val operationsQueue = ConcurrentLinkedQueue<BleOperation>()
    private var pendingOperation: BleOperation? = null
    private var _status = MutableStateFlow(BleConnectionStatus())

    /**
     * The Status of the bluetooth connection via a [StateFlow].
     */
    val status = _status.asStateFlow()

    /**
     * The connect function that starts a ble gatt connection.
     * @param device: the [BleDevice] trying to connect.
     * @param context: the [Context] needed for the [Connect] operation.
     */
    internal fun connect(
            device: BleDevice,
            context: Context
    ) {
        if (device.isConnected()) {
            bleLogger.log(TAG, "Already connected to device ${device.address}.")
        } else {
            enqueueOperation(Connect(device, context.applicationContext))
        }
    }

    private fun BleDevice.isConnected() = deviceGattMap.containsKey(this)

    /**
     * If it should reconnect to the ble device after bluetooth was disabled.
     * @param device: the [BleDevice] trying to reconnect.
     */
    internal fun shouldReconnect(
            device: BleDevice
    ) = device.isConnected()

    /**
     * The reconnect function that starts a ble gatt connection after the bluetooth was disabled
     * and enabled again.
     * @param device: the [BleDevice] trying to reconnect.
     * @param context: the [Context] needed for the re [Connect] operation.
     */
    internal fun reconnect(
            device: BleDevice,
            context: Context
    ) {
        if (device.isConnected()) {
            enqueueOperation(Connect(device, context.applicationContext))
        }
    }

    /**
     * Teardown the ble gatt connection.
     * @param device: the [BleDevice] trying to close the connection.
     */
    fun teardownConnection(
            device: BleDevice
    ) {
        if (device.isConnected()) {
            enqueueOperation(Disconnect(device))
        } else {
            bleLogger.log(
                    TAG, "Not connected to ${device.address}. Cannot teardown the connection."
            )
        }
    }

    /**
     * Reads the specific characteristic [UUID] if possible.
     * @param device: the [BleDevice] trying to read the characteristic from.
     * @param characteristicUuid: the [UUID] trying to read from.
     */
    fun readCharacteristic(
            device: BleDevice,
            characteristicUuid: UUID
    ) {
        if (device.isConnected()) {
            enqueueOperation(CharacteristicRead(device, characteristicUuid))
        } else {
            bleLogger.log(
                    TAG, "Not connected to ${device.address}. Cannot read characteristic."
            )
        }
    }

    /**
     * Writes the specific characteristic [UUID] for the given [payload] if possible.
     * @param device: the [BleDevice] trying to write the [payload] to the characteristic.
     * @param characteristicUuid: the [UUID] trying to write to.
     * @param payload: the[ByteArray] trying to write to characteristic.
     */
    fun writeCharacteristic(
            device: BleDevice,
            characteristicUuid: UUID,
            payload: ByteArray
    ) {
        if (device.isConnected()) {
            enqueueOperation(CharacteristicWrite(device, characteristicUuid, payload))
        } else {
            bleLogger.log(
                    TAG, "Not connected to ${device.address}. Cannot write to characteristic."
            )
        }
    }

    /**
     * Reads the specific descriptor [UUID] if possible.
     * @param device: the [BleDevice] trying to read the descriptor from.
     * @param descriptorUuid: the [UUID] trying to read from.
     */
    fun readDescriptor(
            device: BleDevice,
            descriptorUuid: UUID
    ) {
        if (device.isConnected()) {
            enqueueOperation(DescriptorRead(device, descriptorUuid))
        } else {
            bleLogger.log(
                    TAG, "Not connected to ${device.address}. Cannot perform descriptor read."
            )
        }
    }

    /**
     * Writes the specific descriptor [UUID] for the given [payload] if possible.
     * @param device: the [BleDevice] trying to write the [payload] to the descriptor.
     * @param descriptorUuid: the [UUID] trying to write to.
     * @param payload: the[ByteArray] trying to write to descriptor.
     */
    fun writeDescriptor(
            device: BleDevice,
            descriptorUuid: UUID,
            payload: ByteArray
    ) {
        if (device.isConnected()) {
            enqueueOperation(DescriptorWrite(device, descriptorUuid, payload))
        } else {
            bleLogger.log(
                    TAG, "Not connected to ${device.address}. Cannot write to descriptor."
            )
        }
    }

    /**
     * Enables the notifications for the specific characteristic [UUID] if possible.
     * @param device: the [BleDevice] trying to enable notifications.
     * @param characteristicUuid: the [UUID] trying to enable notifications.
     */
    fun enableNotifications(
            device: BleDevice,
            characteristicUuid: UUID
    ) {
        handleNotifications(device, characteristicUuid)
    }

    private fun handleNotifications(
            device: BleDevice,
            characteristicUuid: UUID,
            isEnable: Boolean = true
    ) {
        if (device.isConnected()) {
            enqueueOperation(
                    if (isEnable) EnableNotifications(device, characteristicUuid)
                    else DisableNotifications(device, characteristicUuid)
            )
        } else {
            bleLogger.log(
                    TAG, "Not connected to ${device.address}. Cannot change notifications"
            )
        }
    }

    /**
     * Disables the notifications for the specific characteristic [UUID] if possible.
     * @param device: the [BleDevice] trying to disable notifications.
     * @param characteristicUuid: the [UUID] trying to disable notifications.
     */
    fun disableNotifications(
            device: BleDevice,
            characteristicUuid: UUID
    ) {
        handleNotifications(device, characteristicUuid, false)
    }

    /**
     * Requests specific mtu for write operations. The default value is the [BLE_GATT_MAX_MTU_SIZE].
     * All values should be between min [BLE_GATT_MIN_MTU_SIZE] or max [BLE_GATT_MAX_MTU_SIZE] else
     * the [Int] give will coerce between them.
     * @param device: the [BleDevice] trying to update the mtu.
     * @param mtu: the [Int] value for the mtu operation.
     */
    fun requestMtu(
            device: BleDevice,
            mtu: Int
    ) {
        if (device.isConnected()) {
            enqueueOperation(
                    MtuRequest(device, mtu.coerceIn(BLE_GATT_MIN_MTU_SIZE, BLE_GATT_MAX_MTU_SIZE))
            )
        } else {
            bleLogger.log(TAG, "Not connected to any device, cannot request MTU update!")
        }
    }

    /**
     * Gets a list of the available [BleGattService] for the gatt connection of the [BleDevice].
     * @param device: the [BleDevice] to get all available services.
     */
    fun getServicesOnDevice(
            device: BleDevice
    ): List<BleGattService> = deviceGattMap[device]?.services ?: emptyList()

    /**
     * Force closing any open connection and Clears the queue of operations and removes
     * last pending one for the given [BleDevice].
     */
    fun forceTearDownDevice(
            device: BleDevice
    ) {
        bleLogger.log(TAG, "Force closing connections and operations to $device.")
        if (!deviceGattMap.contains(device)) {
            bleLogger.log(TAG, "$device doesn't have any open connection.")
            return
        }
        val iterator = operationsQueue.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().bleDevice == device) {
                iterator.remove()
            }
        }
        deviceGattMap.remove(device)
    }

    /**
     * Force closing all open connections and Clears the queue of all operations and removes
     * last pending one.
     */
    fun forceTeardownAll() {
        bleLogger.log(TAG, "Force closing all connections and operations.")
        operationsQueue.clear()
        pendingOperation = null
        for ((_, bleGatt) in deviceGattMap) {
            bleGatt.close()
        }
        deviceGattMap.clear()
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

        val gatt = deviceGattMap[operation.bleDevice]
                ?: this@BleConnection.run {
                    bleLogger.log(
                            TAG, "Not connected to device ${operation.bleDevice.address}." +
                            "Aborting operation."
                    )
                    signalEndOfOperation()
                    return
                }

        when (operation) {
            is Disconnect -> handleDisconnect(operation, gatt)
            is CharacteristicWrite -> handleCharacteristicWrite(operation, gatt)
            is CharacteristicRead -> handleCharacteristicRead(operation, gatt)
            is DescriptorWrite -> handleDescriptorWrite(operation, gatt)
            is DescriptorRead -> handleDescriptorRead(operation, gatt)
            is EnableNotifications -> handleEnableNotifications(operation, gatt)
            is DisableNotifications -> handleDisableNotifications(operation, gatt)
            is MtuRequest -> handleMtuRequest(operation, gatt)
            else -> signalEndOfOperation()
        }
    }

    private fun handleDisconnect(
            operation: Disconnect,
            bleGatt: BleGatt
    ) {
        with(operation) {
            bleLogger.log(TAG, "Disconnecting from device ${bleDevice.address}")
            bleGatt.close()
            deviceGattMap.remove(bleDevice)
            _status.value = BleConnectionStatus()
            signalEndOfOperation()
        }
    }

    private fun handleCharacteristicWrite(
            operation: CharacteristicWrite,
            bleGatt: BleGatt
    ) {
        with(operation) {
            findCharacteristic(bleGatt, characteristicUuid) { characteristic ->
                val writeType = when {
                    characteristic.isWritable() -> BleGattCharacteristic.WRITE_TYPE_DEFAULT
                    characteristic.isWritableWithoutResponse() -> BleGattCharacteristic
                            .WRITE_TYPE_NO_RESPONSE
                    else -> {
                        bleLogger.log(TAG, "")
                        bleLogger.log(
                                TAG, characteristic.uuid.getCharacteristicName() +
                                " isn't writable."
                        )
                        signalEndOfOperation()
                        return@findCharacteristic
                    }
                }
                characteristic.writeType = writeType
                characteristic.value = payload
                bleGatt.writeCharacteristic(characteristic)
            }
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

    private fun handleCharacteristicRead(
            operation: CharacteristicRead,
            bleGatt: BleGatt
    ) {
        with(operation) {
            findCharacteristic(bleGatt, characteristicUuid) { characteristic ->
                if (characteristic.isReadable()) bleGatt.readCharacteristic(characteristic)
                else {
                    bleLogger.log(
                            TAG, characteristic.uuid.getCharacteristicName() +
                            " isn't readable."
                    )
                    signalEndOfOperation()
                }
            }
        }
    }

    private fun handleDescriptorWrite(
            operation: DescriptorWrite,
            bleGatt: BleGatt
    ) {
        with(operation) {
            findDescriptor(bleGatt, descriptorUuid) { descriptor ->
                if (descriptor.isWritable()
                        || descriptor.isClientCharacteristicConfigurationDescriptor()
                ) {
                    bleGatt.writeDescriptor(descriptor)
                } else {
                    bleLogger.log(
                            TAG, "${descriptor.uuid.getDescriptorName()} isn't writable."
                    )
                    signalEndOfOperation()
                }
            }
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

    private fun handleDescriptorRead(
            operation: DescriptorRead,
            bleGatt: BleGatt
    ) {
        with(operation) {
            findDescriptor(bleGatt, descriptorUuid) { descriptor ->
                if (descriptor.isReadable()) {
                    bleGatt.readDescriptor(descriptor)
                } else {
                    bleLogger.log(
                            TAG, descriptor.uuid.getDescriptorName() +
                            " isn't readable."
                    )
                    signalEndOfOperation()
                }
            }
        }
    }

    private fun handleEnableNotifications(
            operation: EnableNotifications,
            bleGatt: BleGatt
    ) {
        with(operation) {
            findCharacteristic(bleGatt, characteristicUuid) { characteristic ->
                val uuid =
                        BleGattDescriptorUuids.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuid()
                val payload = when {
                    characteristic.isIndicatable() ->
                        BleGattDescriptor.ENABLE_INDICATION_VALUE
                    characteristic.isNotifiable() ->
                        BleGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    else -> {
                        bleLogger.log(
                                TAG, characteristic.uuid.getCharacteristicName() +
                                " doesn't support notifications / indications"
                        )
                        signalEndOfOperation()
                        return@findCharacteristic
                    }
                }
                characteristic.getDescriptor(uuid)?.let { descriptor ->
                    if (!bleGatt.setCharacteristicNotification(characteristic, true)) {
                        bleLogger.log(
                                TAG, "Setting notification failed for " +
                                characteristic.uuid.getDescriptorName()
                        )
                        signalEndOfOperation()
                        return@findCharacteristic
                    }

                    descriptor.value = payload
                    bleGatt.writeDescriptor(descriptor)
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
    }

    private fun handleDisableNotifications(
            operation: DisableNotifications,
            bleGatt: BleGatt
    ) {
        with(operation) {
            findCharacteristic(bleGatt, characteristicUuid) { characteristic ->
                if (!characteristic.isIndicatable() && !characteristic.isNotifiable()) {
                    bleLogger.log(
                            TAG, characteristic.uuid.getCharacteristicName() +
                            " doesn't support notifications / indications"
                    )
                    signalEndOfOperation()
                    return@findCharacteristic
                }
                val uuid =
                        BleGattDescriptorUuids.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuid()
                characteristic.getDescriptor(uuid)?.let { cccDescriptor ->
                    if (!bleGatt.setCharacteristicNotification(characteristic, false)) {
                        bleLogger.log(
                                TAG, "Setting notification failed for " +
                                characteristic.uuid.getDescriptorName()
                        )
                        signalEndOfOperation()
                        return@findCharacteristic
                    }

                    cccDescriptor.value = BleGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    bleGatt.writeDescriptor(cccDescriptor)
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
    }

    private fun handleMtuRequest(
            operation: MtuRequest,
            bleGatt: BleGatt
    ) {
        with(operation) {
            bleGatt.requestMtu(mtu)
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
                        deviceGattMap[bleGatt.device] = bleGatt
                        coroutineScope.launch { bleGatt.discoverServices() }
                    } else if (newState == BleProfile.STATE_DISCONNECTED) {
                        bleLogger.log(TAG, "Successfully disconnected from $deviceAddress")
                        teardownConnection(bleGatt.device)
                    }
                }
                BleGatt.GATT_INSUFFICIENT_ENCRYPTION,
                BleGatt.GATT_INSUFFICIENT_AUTHENTICATION -> {
                    bleLogger.log(TAG, "Trying to bond with $deviceAddress")
                    bleGatt.device.createBond()
                }
                else -> {
                    bleLogger.log(
                        TAG, "Failed to connected with error: ${status.getBleGattStatus()}"
                    )
                    if (pendingOperation is Connect) {
                        signalEndOfOperation()
                    }
                    teardownConnection(bleGatt.device)
                }
            }
        }

        override fun onServicesDiscovered(
                bleGatt: BleGatt,
                status: Int
        ) {
            with(bleGatt) {
                if (status == BleGatt.GATT_SUCCESS) {
                    bleLogger.log(
                            TAG, "Discovered ${services.size} services for " +
                            "device ${device.address}."
                    )
                    printGattInformation(bleLogger, TAG)
                    requestMtu(bleGatt.device, BLE_GATT_MAX_MTU_SIZE)
                    _status.value = _status.value.copy(isConnected = true, bleDevice = device)
                } else {
                    bleLogger.log(TAG, "Service discovery failed due to status $status")
                    teardownConnection(device)
                }
            }

            if (pendingOperation is Connect) {
                signalEndOfOperation()
            }
        }

        override fun onMtuChanged(
                bleGatt: BleGatt,
                mtu: Int,
                status: Int
        ) {
            bleLogger.log(
                    TAG, "Mtu changed to $mtu, success: ${status == BleGatt.GATT_SUCCESS}"
            )
            _status.value = _status.value.copy(mtuRequestValue = mtu)
            if (pendingOperation is MtuRequest) {
                signalEndOfOperation()
            }
        }

        override fun onCharacteristicRead(
                bleGatt: BleGatt,
                characteristic: BleGattCharacteristic,
                status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BleGatt.GATT_SUCCESS -> {
                        bleLogger.log(
                                TAG, "Read characteristic " +
                                "${uuid.getCharacteristicName()}|value: ${value.toHexString()}"
                        )
                        _status.value = _status.value.copy(bleGattCharacteristic = this)
                    }
                    BleGatt.GATT_READ_NOT_PERMITTED -> {
                        bleLogger.log(
                                TAG, "Read not permitted for ${uuid.getCharacteristicName()}."
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
                bleGatt: BleGatt,
                characteristic: BleGattCharacteristic,
                status: Int
        ) {
            with(characteristic) {
                when (status) {
                    BleGatt.GATT_SUCCESS -> {
                        bleLogger.log(
                                TAG, "Wrote to characteristic ${uuid.getCharacteristicName()}" +
                                " | value: ${value.toHexString()}"
                        )
                        _status.value = _status.value.copy(bleGattCharacteristic = this)
                    }
                    BleGatt.GATT_WRITE_NOT_PERMITTED -> {
                        bleLogger.log(
                                TAG, "Write not permitted for ${uuid.getCharacteristicName()}."
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
                bleGatt: BleGatt,
                characteristic: BleGattCharacteristic
        ) {
            with(characteristic) {
                bleLogger.log(
                        TAG, "Characteristic ${uuid.getCharacteristicName()} " +
                        "changed | value: ${value.toHexString()}"
                )
                _status.value = _status.value.copy(bleGattCharacteristic = this)
            }
        }

        override fun onDescriptorRead(
                bleGatt: BleGatt,
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
                        _status.value = _status.value.copy(bleGattDescriptor = this)
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
                bleGatt: BleGatt,
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
                                    value, characteristic
                            )
                        } else {
                            _status.value = _status.value.copy(bleGattDescriptor = this)
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
                    _status.value = _status.value.copy(bleGattCharacteristic = characteristic)
                }
                notificationsDisabled -> {
                    bleLogger.log(
                            TAG, "Notifications or indications disabled on " +
                            characteristicUuid.getCharacteristicName()
                    )
                    _status.value = _status.value.copy(bleGattCharacteristic = characteristic)
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
