package com.steleot.blekotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.steleot.blekotlin.internal.UNKNOWN_STATE
import com.steleot.blekotlin.internal.UNKNOWN_STATUS
import com.steleot.blekotlin.internal.utils.bluetoothBondStates
import com.steleot.blekotlin.internal.utils.bluetoothStatuses

private const val TAG = "BluetoothReceiver"

abstract class BleReceiver(
    protected val bleLogger: BleLogger,
    protected val callbacks: BleReceiverListener
) : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        when (intent.action) {
            BleAdapter.ACTION_STATE_CHANGED -> bluetoothStateChanged(intent)
            BleDevice.ACTION_BOND_STATE_CHANGED -> bluetoothBondStateChanged(intent)
        }
    }

    private fun bluetoothStateChanged(
        intent: Intent
    ) {
        val previousState = intent.getIntExtra(
            BleAdapter.EXTRA_PREVIOUS_STATE,
            BleAdapter.ERROR
        )
        val nextState = intent.getIntExtra(
            BleAdapter.EXTRA_STATE,
            BleAdapter.ERROR
        )
        bleLogger.log(
            TAG,
            "Previous state is $previousState ${
                bluetoothStatuses.getOrElse(previousState) {
                    UNKNOWN_STATUS
                }
            } and next state is $nextState ${
                bluetoothStatuses.getOrElse(nextState) {
                    UNKNOWN_STATUS
                }
            }"
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
        val device = intent.getParcelableExtra<BleDevice>(BleDevice.EXTRA_DEVICE)
        val previousState = intent.getIntExtra(
            BleDevice.EXTRA_PREVIOUS_BOND_STATE,
            BleDevice.BOND_NONE
        )
        val nextState = intent.getIntExtra(
            BleDevice.EXTRA_BOND_STATE,
            BleDevice.BOND_NONE
        )
        bleLogger.log(
            TAG,
            "Previous state is $previousState ${
                bluetoothBondStates.getOrElse(previousState) {
                    UNKNOWN_STATE
                }
            } and next state is $nextState ${
                bluetoothBondStates.getOrElse(nextState) {
                    UNKNOWN_STATE
                }
            }"
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
