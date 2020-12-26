package com.steleot.blekotlin.status

import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleGattDescriptor

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
    val bleGattCharacteristic: BleGattCharacteristic? = null,

    /**
     * The latest bluetooth gatt descriptor.
     */
    val bleGattDescriptor: BleGattDescriptor? = null,

    /**
     * The last mtu request value.
     */
    val mtuRequestValue: Int? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as BleConnectionStatus
        if (isConnected != other.isConnected) return false
        if (bleDevice != other.bleDevice) return false
        /*
        Need to update every time the stateflow so the below always return false.
        if (bleGattCharacteristic != other.bleGattCharacteristic) return false
        if (bleGattDescriptor != other.bleGattDescriptor) return false
        if (mtuRequestValue != other.mtuRequestValue) return false
        */
        return false
    }

    override fun hashCode(): Int {
        var result = isConnected.hashCode()
        result = 31 * result + bleDevice.hashCode()
        result = 31 * result + bleGattCharacteristic?.uuid.hashCode()
        result = 31 * result + bleGattDescriptor?.uuid.hashCode()
        result = 31 * result + mtuRequestValue.hashCode()
        return result
    }
}
