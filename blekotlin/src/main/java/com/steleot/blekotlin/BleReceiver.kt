package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Logger tag constant.
 */
private const val TAG = "BluetoothReceiver"

internal class BleReceiver(
    private val logger: BleLogger,
    private val callbacks: BleReceiverCallbacks
) : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        when (intent.action) {
            ACTION_STATE_CHANGED -> bluetoothStateChanged(intent)
            ACTION_BOND_STATE_CHANGED -> bluetoothBondStateChanged(intent)
        }
    }

    private fun bluetoothStateChanged(
        intent: Intent
    ) {
        val previousState = intent.getIntExtra(
            BluetoothAdapter.EXTRA_PREVIOUS_STATE,
            BluetoothAdapter.ERROR
        )
        val nextState = intent.getIntExtra(
            BluetoothAdapter.EXTRA_STATE,
            BluetoothAdapter.ERROR
        )
        logger.log(
            TAG,
            "Previous state is $previousState ${bluetoothStatuses.getOrElse(previousState) { UNKNOWN_STATUS }} " +
                    "and next state is $nextState ${bluetoothStatuses.getOrElse(nextState) { UNKNOWN_STATUS }}"
        )

        if (nextState == BluetoothAdapter.STATE_ON) {
            callbacks.bluetoothStatus(true)
        } else if (nextState == BluetoothAdapter.STATE_OFF) {
            callbacks.bluetoothStatus(false)
        }
    }

    private fun bluetoothBondStateChanged(
        intent: Intent
    ) {
        // todo stleios
    }

    interface BleReceiverCallbacks {
        fun bluetoothStatus(isEnabled: Boolean)
    }
}
