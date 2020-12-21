package com.steleot.blekotlin.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.steleot.blekotlin.BleAdapter
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.utils.getBleBondState
import com.steleot.blekotlin.internal.utils.getBleState
import com.steleot.blekotlin.receiver.BleReceiver.BleReceiverListener

private const val TAG = "BluetoothReceiver"

/**
 * An abstract implementation of [BroadcastReceiver] that handles
 * [BluetoothAdapter.ACTION_STATE_CHANGED] and [BluetoothDevice.ACTION_BOND_STATE_CHANGED] with
 * abstract functions. Use [BleReceiverListener] to correctly connect with
 * [com.steleot.blekotlin.BleClient]..
 */
abstract class BleReceiver(
    protected val bleLogger: BleLogger,
    protected val listener: BleReceiverListener
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

    /**
     * Handles bluetooth state changed.
     * @param previousState: [Int] showing the previous bluetooth state.
     * @param nextState: [Int] showing the next bluetooth state.
     */
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

    /**
     * Handles bluetooth device bond state changed.
     * @param previousState: [Int] showing the previous bluetooth device bond state.
     * @param nextState: [Int] showing the next bluetooth device bond state.
     */
    abstract fun handleBleBondStateChanged(
        bleDevice: BleDevice,
        previousState: Int,
        nextState: Int
    )

    /**
     * The listener interface that connects the [BleReceiver] with the
     * [com.steleot.blekotlin.BleClient]..
     */
    interface BleReceiverListener {

        /**
         * Checks bluetooth status.
         * @param isEnabled: [Boolean] showing if bluetooth is enabled.
         */
        fun bleStatus(isEnabled: Boolean)

        /**
         * Checks bluetooth bond state.
         * @param isBonded: [Boolean] showing if bluetooth device bond state is bonded.
         */
        fun bleBondState(isBonded: Boolean)
    }
}
