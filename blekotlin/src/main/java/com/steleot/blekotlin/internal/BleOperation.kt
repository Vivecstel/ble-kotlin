package com.steleot.blekotlin.internal

import android.content.Context
import com.steleot.blekotlin.BleDevice
import java.util.UUID

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

internal data class EnableNotifications(
    override val bleDevice: BleDevice,
    val characteristicUuid: UUID
) : BleOperation()

internal data class DisableNotifications(
    override val bleDevice: BleDevice,
    val characteristicUuid: UUID
) : BleOperation()
