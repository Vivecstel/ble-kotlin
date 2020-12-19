package com.steleot.blekotlin.internal.callback

import android.bluetooth.BluetoothProfile
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleGattCallback
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.internal.UNKNOWN_STATUS
import com.steleot.blekotlin.internal.utils.gattStatuses
import com.steleot.blekotlin.internal.utils.printGattInformation

private const val TAG = "BleGattCallback"

internal class BleDefaultGattCallback(
    private val bleLogger: BleLogger,
    private val listener: BleGattCallbackListener
) : BleGattCallback() {

    override fun onConnectionStateChange(
        gatt: BleGatt,
        status: Int,
        newState: Int
    ) {
        val deviceAddress = gatt.device.address
        if (status == BleGatt.GATT_SUCCESS) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bleLogger.log(
                    TAG, "Successfully connected to $deviceAddress"
                )
                listener.onGattSuccess(gatt)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                bleLogger.log(
                    TAG, "Successfully disconnected from $deviceAddress"
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
            this.printGattInformation(bleLogger, TAG)
        }
    }

    interface BleGattCallbackListener {
        fun onGattSuccess(bleGatt: BleGatt)
        fun onGattFailure()
    }
}