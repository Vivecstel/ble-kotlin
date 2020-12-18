package com.steleot.blekotlin.internal.receiver

import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.BleReceiver

private const val TAG = "EmptyBleReceiver"

internal class EmptyBleReceiver(
    bleLogger: BleLogger,
    callbacks: BleReceiverListener
) : BleReceiver(bleLogger, callbacks) {

    override fun handleBluetoothStateChanged(
        previousState: Int,
        nextState: Int
    ) {
        bleLogger.log(TAG, "Doing nothing for handling bluetooth state changed")
    }

    override fun handleBluetoothBondStateChanged(
        previousStatus: Int,
        nextStatus: Int
    ) {
        bleLogger.log(TAG, "Doing nothing for handling bluetooth state changed")
    }
}
