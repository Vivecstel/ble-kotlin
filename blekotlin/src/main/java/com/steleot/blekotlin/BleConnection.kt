package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.content.Context
import com.steleot.blekotlin.internal.BLE_GATT_MAX_MTU_SIZE
import com.steleot.blekotlin.internal.BleDescriptors
import com.steleot.blekotlin.internal.BleOperation
import com.steleot.blekotlin.internal.CONNECT_DELAY
import com.steleot.blekotlin.internal.Connect
import com.steleot.blekotlin.internal.DisableNotifications
import com.steleot.blekotlin.internal.Disconnect
import com.steleot.blekotlin.internal.EnableNotifications
import com.steleot.blekotlin.internal.utils.findCharacteristic
import com.steleot.blekotlin.internal.utils.getCharacteristicName
import com.steleot.blekotlin.internal.utils.getDescriptorName
import com.steleot.blekotlin.internal.utils.isIndicatable
import com.steleot.blekotlin.internal.utils.isNotifiable
import com.steleot.blekotlin.internal.utils.printGattInformation
import com.steleot.blekotlin.internal.utils.toBluetoothUuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        if (device.isConnected()) {
            bleLogger.log(TAG, "Already connected to device ${device.address}")
        } else {
            enqueueOperation(Connect(device, context.applicationContext))
        }
    }

    private fun BleDevice.isConnected() = bleGatt != null
            && this.address == bleGatt!!.device.address

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
            bleLogger.log(TAG, "Not connected to any device. Cannot " +
                    "teardown the connection.")
        }
    }

    fun enableNotifications(
        device: BleDevice,
        characteristic: BleGattCharacteristic
    ) {
        handleNotifications(device, characteristic)
    }

    private fun handleNotifications(
        device: BleDevice,
        characteristic: BleGattCharacteristic,
        isEnable: Boolean = true
    ) {
        if (device.isConnected() &&
            (characteristic.isIndicatable() || characteristic.isNotifiable())
        ) {
            enqueueOperation(if (isEnable) EnableNotifications(device, characteristic.uuid)
            else DisableNotifications(device, characteristic.uuid))
        } else if (!device.isConnected()) {
            bleLogger.log(TAG, "Not connected to device ${device.address}," +
                    " cannot change notifications")
        } else if (!characteristic.isIndicatable() && !characteristic.isNotifiable()) {
            bleLogger.log(TAG,
                "${characteristic.uuid.getCharacteristicName()} doesn't support " +
                        "notifications/indications")
        }
    }

    fun disableNotifications(
        device: BleDevice,
        characteristic: BleGattCharacteristic
    ) {
        handleNotifications(device, characteristic, false)
    }

    fun requestMtu(
        device: BleDevice,
        mtu: Int
    ) {
//        if (device.isConnected()) {
//            enqueueOperation(MtuRequest(device, mtu.coerceIn(GATT_MIN_MTU_SIZE, GATT_MAX_MTU_SIZE)))
//        } else {
//            Timber.e("Not connected to ${device.address}, cannot request MTU update!")
//        }
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
            bleLogger.log(TAG, "Not connected to device ${operation.bleDevice.address}." +
                    "Aborting operation.")
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
            is EnableNotifications -> with(operation) {
                gatt.findCharacteristic(characteristicUuid)?.let { characteristic ->
                    val uuid = BleDescriptors.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuid()
                    val payload = when {
                        characteristic.isIndicatable() ->
                            BleGattDescriptor.ENABLE_INDICATION_VALUE
                        characteristic.isNotifiable() ->
                            BleGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        else ->
                            error("${characteristic.uuid.getCharacteristicName()} doesn't " +
                                    "support notifications / indications")
                    }
                    characteristic.getDescriptor(uuid)?.let { descriptor ->
                        if (!gatt.setCharacteristicNotification(characteristic, true)) {
                            bleLogger.log(TAG, "Setting notification failed for " +
                                    characteristic.uuid.getDescriptorName()
                            )
                            signalEndOfOperation()
                            return
                        }

                        descriptor.value = payload
                        gatt.writeDescriptor(descriptor)
                    } ?: run {
                        bleLogger.log(TAG, "Cannot find " +
                                "${characteristicUuid.getCharacteristicName()}. Failed to enable" +
                                " notifications.")
                        signalEndOfOperation()
                    }
                }
            }
            is DisableNotifications -> with(operation) {

            }
//            is MtuRequest -> with(operation) {
//                gatt.requestMtu(mtu)
//            }
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
                    bleLogger.log(TAG, "Discovered ${services.size} services for " +
                            "device ${device.address}.")
                    printGattInformation(bleLogger, TAG)
                    requestMtu(device, BLE_GATT_MAX_MTU_SIZE)
                } else {
                    bleLogger.log(TAG, "Service discovery failed due to status $status")
                    teardownConnection()
                }
            }

            if (pendingOperation is Connect) {
                signalEndOfOperation()
            }
        }
    }
}