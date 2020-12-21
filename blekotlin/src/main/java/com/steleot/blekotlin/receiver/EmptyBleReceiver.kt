package com.steleot.blekotlin.receiver

import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.helper.BleLogger

private const val TAG = "EmptyBleReceiver"

/**
 * An empty implementation of [BleReceiver]. Using this is assumed that [BleReceiver] is not needed.
 */
internal class EmptyBleReceiver(
    bleLogger: BleLogger,
    listener: BleReceiverListener
) : BleReceiver(bleLogger, listener) {

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