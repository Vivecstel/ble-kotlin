package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.steleot.blekotlin.internal.UNKNOWN_STATE
import com.steleot.blekotlin.internal.UNKNOWN_STATUS
import com.steleot.blekotlin.internal.utils.bluetoothBondStates
import com.steleot.blekotlin.internal.utils.bluetoothStatuses

/**
 * Logger tag constant.
 */
private const val TAG = "BluetoothReceiver"

abstract class BleReceiver(
    protected val logger: BleLogger,
    protected val callbacks: BleReceiverListener
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

        handleBluetoothStateChanged(previousState, nextState)
    }

    abstract fun handleBluetoothStateChanged(
        previousState: Int,
        nextState: Int
    )

    private fun bluetoothBondStateChanged(
        intent: Intent
    ) {
        val previousState = intent.getIntExtra(
            BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE,
            BluetoothDevice.BOND_NONE
        )
        val nextState = intent.getIntExtra(
            BluetoothDevice.EXTRA_BOND_STATE,
            BluetoothDevice.BOND_NONE
        )
        logger.log(
            TAG,
            "Previous state is $previousState ${bluetoothBondStates.getOrElse(previousState) { UNKNOWN_STATE }} " +
                    "and next state is $nextState ${bluetoothBondStates.getOrElse(nextState) { UNKNOWN_STATE }}"
        )
        handleBluetoothBondStateChanged(previousState, nextState)
    }

    abstract fun handleBluetoothBondStateChanged(
        previousStatus: Int,
        nextStatus: Int
    )

    interface BleReceiverListener {
        fun bluetoothStatus(isEnabled: Boolean)
    }
}
