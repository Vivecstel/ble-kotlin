package com.steleot.blekotlin.status

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.steleot.blekotlin.BleDevice

/**
 * A data class representing the status of the [com.steleot.blekotlin.BleConnection].
 * You can subscribe to this in [com.steleot.blekotlin.BleConnection].
 */
data class BleConnectionStatus(

    /**
     * Check if connection is established correctly.
     */
    val isConnected: Boolean = false,

    /**
     * The latest bluetooth device connected.
     */
    val bleDevice: BleDevice? = null,

    /**
     * The latest bluetooth gatt characteristic.
     */
    val bleGattCharacteristic: BluetoothGattCharacteristic? = null,

    /**
     * The latest bluetooth gatt descriptor.
     */
    val bleGattDescriptor: BluetoothGattDescriptor? = null,

    /**
     * The last mtu request value.
     */
    val mtuRequestValue: Int? = null
)
