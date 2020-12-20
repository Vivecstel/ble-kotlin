package com.steleot.blekotlin.internal.receiver

import com.steleot.blekotlin.BleAdapter
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.receiver.BleReceiver

internal class DefaultBleReceiver(
    bleLogger: BleLogger,
    callbacks: BleReceiverListener
) : BleReceiver(bleLogger, callbacks) {

    override fun handleBleStateChanged(
        previousState: Int,
        nextState: Int
    ) {
        if (nextState == BleAdapter.STATE_ON) {
            callbacks.bleStatus(true)
        } else if (nextState == BleAdapter.STATE_OFF) {
            callbacks.bleStatus(false)
        }
    }

    override fun handleBleBondStateChanged(
        bleDevice: BleDevice,
        previousState: Int,
        nextState: Int
    ) {
        if (nextState == BleDevice.BOND_BONDED) {
            callbacks.bleBondState(true)
        } else if (nextState == BleDevice.BOND_NONE) {
            callbacks.bleBondState(false)
        }
    }
}