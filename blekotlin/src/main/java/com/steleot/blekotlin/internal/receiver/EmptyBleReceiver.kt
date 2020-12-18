package com.steleot.blekotlin.internal.receiver

import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.BleReceiver

/**
 * Logger tag constant.
 */
private const val TAG = "EmptyBleReceiver"

internal class EmptyBleReceiver(
    logger: BleLogger,
    callbacks: BleReceiverListener
) : BleReceiver(logger, callbacks) {

    override fun handleBluetoothStateChanged(
        previousState: Int,
        nextState: Int
    ) {
        logger.log(TAG, "Doing nothing for handling bluetooth state changed")
    }

    override fun handleBluetoothBondStateChanged(
        previousStatus: Int,
        nextStatus: Int
    ) {
        logger.log(TAG, "Doing nothing for handling bluetooth state changed")
    }
}
