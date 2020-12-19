package com.steleot.blekotlin.internal.receiver

import com.steleot.blekotlin.BleAdapter
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
        if (nextState == BleAdapter.STATE_ON) {
            callbacks.bluetoothStatus(true)
        } else if (nextState == BleAdapter.STATE_OFF) {
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