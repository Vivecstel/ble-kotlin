package com.steleot.blekotlin.internal.receiver

import android.bluetooth.BluetoothAdapter
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.BleReceiver

internal class DefaultBleReceiver(
    bleLogger: BleLogger,
    callbacks: BleReceiverListener
) : BleReceiver(bleLogger, callbacks) {

    override fun handleBluetoothStateChanged(
        previousState: Int,
        nextState: Int
    ) {
        if (nextState == BluetoothAdapter.STATE_ON) {
            callbacks.bluetoothStatus(true)
        } else if (nextState == BluetoothAdapter.STATE_OFF) {
            callbacks.bluetoothStatus(false)
        }
    }

    override fun handleBluetoothBondStateChanged(
        previousStatus: Int,
        nextStatus: Int
    ) {
        TODO()
    }
}