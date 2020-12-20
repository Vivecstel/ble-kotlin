package com.steleot.blekotlin

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

data class BleConnectionStatus(
    val isConnected: Boolean = false,
    val bleDevice: BleDevice? = null,
    val bleGattCharacteristic: BluetoothGattCharacteristic? = null,
    val bleGattDescriptor: BluetoothGattDescriptor? = null,
    val mtuRequestValue: Int? = null
)
