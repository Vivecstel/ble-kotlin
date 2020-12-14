package com.steleot.blekotlin

import android.bluetooth.BluetoothManager
import android.content.Context
import kotlinx.coroutines.flow.flow

object BleKotlin {

    private var bluetoothManager: BluetoothManager? = null
    private var status: BleStatus = BleStatus.NotStarted()

    fun init(
        context: Context
    ) {
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    }

    suspend fun getState() = flow {
        emit(status)
    }

    fun startBleScan() {
        if (bluetoothManager == null) {
            status = BleStatus.BluetoothNotAvailable()
        }
    }
}
