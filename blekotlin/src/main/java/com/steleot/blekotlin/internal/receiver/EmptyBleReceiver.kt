package com.steleot.blekotlin.internal.receiver

import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.BleReceiver

private const val TAG = "EmptyBleReceiver"

internal class EmptyBleReceiver(
    bleLogger: BleLogger,
    callbacks: BleReceiverListener
) : BleReceiver(bleLogger, callbacks) {

    override fun handleBleStateChanged(
        previousState: Int,
        nextState: Int
    ) {
        bleLogger.log(TAG, "Doing nothing for handling ble state changed")
    }

    override fun handleBleBondStateChanged(
        bleDevice: BleDevice,
        previousState: Int,
        nextState: Int
    ) {
        bleLogger.log(TAG, "Doing nothing for handling ble device bond state changed")
    }
}
