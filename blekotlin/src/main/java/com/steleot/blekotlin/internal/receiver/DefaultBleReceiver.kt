package com.steleot.blekotlin.internal.receiver

import com.steleot.blekotlin.BleAdapter
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.receiver.BleReceiver

/**
 * The default implementation of [BleReceiver]. The default implementation for the
 * [com.steleot.blekotlin.BleConfig] receiver if not a custom one is used.
 */
internal class DefaultBleReceiver(
    bleLogger: BleLogger,
    listener: BleReceiverListener
) : BleReceiver(bleLogger, listener) {

    override fun handleBleStateChanged(
        previousState: Int,
        nextState: Int
    ) {
        if (nextState == BleAdapter.STATE_ON) {
            listener.bleStatus(true)
        } else if (nextState == BleAdapter.STATE_OFF) {
            listener.bleStatus(false)
        }
    }

    override fun handleBleBondStateChanged(
        bleDevice: BleDevice,
        previousState: Int,
        nextState: Int
    ) {
        if (nextState == BleDevice.BOND_BONDED) {
            listener.bleBondState(true)
        } else if (nextState == BleDevice.BOND_NONE) {
            listener.bleBondState(false)
        }
    }
}