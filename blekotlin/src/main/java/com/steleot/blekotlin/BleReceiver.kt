package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val TAG = "BluetoothReceiver"

internal class BleReceiver(
    private val logger: BleLogger,
    private val callbacks: BleReceiverCallbacks
) : BroadcastReceiver() {

    private val bluetoothStatuses by lazy {
        mapOf(
            BluetoothAdapter.STATE_OFF to "Bluetooth state off",
            BluetoothAdapter.STATE_TURNING_ON to "Bluetooth state turning on",
            BluetoothAdapter.STATE_ON to "Bluetooth state on",
            BluetoothAdapter.STATE_TURNING_OFF to "Bluetooth state turning off"
        )
    }

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
            TAG, "Previous state is ${bluetoothStatuses[previousState]} " +
                    "and next state is ${bluetoothStatuses[nextState]}"
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
