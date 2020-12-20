package com.steleot.blekotlin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.steleot.blekotlin.BleAdapter
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.utils.getBleBondState
import com.steleot.blekotlin.internal.utils.getBleState

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
            "Previous state is ${previousState.getBleState()} and next state is " +
                    nextState.getBleState()
        )

        handleBleStateChanged(previousState, nextState)
    }

    abstract fun handleBleStateChanged(
        previousState: Int,
        nextState: Int
    )

    private fun bluetoothBondStateChanged(
        intent: Intent
    ) {
        val device = intent.getParcelableExtra<BleDevice>(BleDevice.EXTRA_DEVICE)
        device?.let {
            val previousState = intent.getIntExtra(
                BleDevice.EXTRA_PREVIOUS_BOND_STATE,
                BleDevice.BOND_NONE
            )
            val nextState = intent.getIntExtra(
                BleDevice.EXTRA_BOND_STATE,
                BleDevice.BOND_NONE
            )
            bleLogger.log(
                TAG, "Device ${device.address} previous bond state is " +
                        "${previousState.getBleBondState()} and next bond state is " +
                        nextState.getBleBondState()
            )
            handleBleBondStateChanged(device, previousState, nextState)
        }
    }

    abstract fun handleBleBondStateChanged(
        bleDevice: BleDevice,
        previousState: Int,
        nextState: Int
    )

    interface BleReceiverListener {
        fun bleStatus(isEnabled: Boolean)
        fun bleBondState(isBonded: Boolean)
    }
}
