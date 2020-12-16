package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val TAG = "BluetoothReceiver"
private const val UNKNOWN_STATUS = "Unknown status"

internal class BleReceiver(
    private val logger: BleLogger,
    private val callbacks: BleReceiverCallbacks
) : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        when (intent.action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> bluetoothStateChanged(intent)
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
            TAG, "Previous state is $previousState ${bluetoothStatuses.getOrElse(previousState) { UNKNOWN_STATUS }} " +
                    "and next state is $nextState ${bluetoothStatuses.getOrElse(nextState) { UNKNOWN_STATUS }}"
        )

        if (nextState == BluetoothAdapter.STATE_ON) {
            callbacks.bluetoothStatus(true)
        } else if (nextState == BluetoothAdapter.STATE_OFF) {
            callbacks.bluetoothStatus(false)
        }
    }

    interface BleReceiverCallbacks {
        fun bluetoothStatus(isEnabled: Boolean)
    }
}
