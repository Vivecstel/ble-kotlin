package com.steleot.blekotlin.internal

import android.content.Context
import com.steleot.blekotlin.BleDevice
import java.util.*

sealed class BleOperation {
    abstract val bleDevice: BleDevice
}

internal data class Connect(
    override val bleDevice: BleDevice,
    val context: Context
) : BleOperation()

internal data class Disconnect(
    override val bleDevice: BleDevice
) : BleOperation()

internal data class CharacteristicWrite(
    override val bleDevice: BleDevice,
    val characteristicUuid: UUID,
    val payload: ByteArray
) : BleOperation() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CharacteristicWrite
        if (bleDevice != other.bleDevice) return false
        if (characteristicUuid != other.characteristicUuid) return false
        if (!payload.contentEquals(other.payload)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = bleDevice.hashCode()
        result = 31 * result + characteristicUuid.hashCode()
        result = 31 * result + payload.contentHashCode()
        return result
    }
}

internal data class CharacteristicRead(
    override val bleDevice: BleDevice,
    val characteristicUuid: UUID
) : BleOperation()

internal data class DescriptorWrite(
    override val bleDevice: BleDevice,
    val descriptorUuid: UUID,
    val payload: ByteArray
) : BleOperation() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DescriptorWrite
        if (bleDevice != other.bleDevice) return false
        if (descriptorUuid != other.descriptorUuid) return false
        if (!payload.contentEquals(other.payload)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = bleDevice.hashCode()
        result = 31 * result + descriptorUuid.hashCode()
        result = 31 * result + payload.contentHashCode()
        return result
    }
}

internal data class DescriptorRead(
    override val bleDevice: BleDevice,
    val descriptorUuid: UUID
) : BleOperation()

internal data class EnableNotifications(
    override val bleDevice: BleDevice,
    val characteristicUuid: UUID
) : BleOperation()

internal data class DisableNotifications(
    override val bleDevice: BleDevice,
    val characteristicUuid: UUID
) : BleOperation()

data class MtuRequest(
    override val bleDevice: BleDevice,
    val mtu: Int
) : BleOperation()
