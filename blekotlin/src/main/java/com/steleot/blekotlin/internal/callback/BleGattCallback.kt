package com.steleot.blekotlin.internal.callback

import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.internal.UNKNOWN_STATUS
import com.steleot.blekotlin.internal.utils.gattStatuses

private const val TAG = "BleGattCallback"

internal class BleGattCallback(
    private val bleLogger: BleLogger,
    private val listener: BleGattCallbackListener
) : BluetoothGattCallback() {

    override fun onConnectionStateChange(
        gatt: BleGatt,
        status: Int,
        newState: Int
    ) {
        val deviceAddress = gatt.device.address
        if (status == BleGatt.GATT_SUCCESS) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bleLogger.log(
                    "BluetoothGattCallback",
                    "Successfully connected to $deviceAddress"
                )
                listener.onGattSuccess(gatt)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                bleLogger.log(
                    "BluetoothGattCallback",
                    "Successfully disconnected from $deviceAddress"
                )
                gatt.close()
            }
        } else {
            bleLogger.log(
                TAG,
                "Error $status ${gattStatuses.getOrElse(status) { UNKNOWN_STATUS }}" +
                        " encountered for $deviceAddress. Disconnecting device.."
            )
            listener.onGattFailure()
        }
    }

    override fun onServicesDiscovered(
        gatt: BleGatt,
        status: Int
    ) {
        with(gatt) {
            bleLogger.log(
                "BluetoothGattCallback",
                "Discovered ${services.size} services for ${device.address}"
            )
        }
    }

    interface BleGattCallbackListener {
        fun onGattSuccess(gatt: BleGatt)
        fun onGattFailure()
    }
}