package com.steleot.blekotlin.utils

import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleManufacturerData
import com.steleot.blekotlin.BleScanResult
import com.steleot.blekotlin.internal.UNKNOWN_UUID
import com.steleot.blekotlin.internal.UUID_16_BIT_LENGTH
import com.steleot.blekotlin.internal.utils.bleCompanies
import com.steleot.blekotlin.internal.utils.gattCharacteristicUuids
import com.steleot.blekotlin.internal.utils.gattDescriptorUuids
import com.steleot.blekotlin.internal.utils.gattServicesUuids
import com.steleot.blekotlin.internal.utils.getStandardizedUuidAsString
import java.util.UUID

/**
 * todo stelios
 */
fun BleScanResult.getManufacturerData(): BleManufacturerData? {
    this.scanRecord?.let {
        for (index in 0 until it.manufacturerSpecificData.size()) {
            val key = it.manufacturerSpecificData.keyAt(index)
            val value = it.manufacturerSpecificData.valueAt(index)
            return key to value
        }
    }
    return null
}

/**
 * Extension function that returns the bluetooth manufacturer name for logging purposes.
 */
fun Int.getManufacturerName(): String {
    val name = bleCompanies.getOrElse(this) { UNKNOWN_UUID }
    return "$name - $this"
}

/**
 * Extension function that returns the [UUID] service name for logging purposes.
 */
fun UUID.getServiceName(): String {
    val name = gattServicesUuids
        .getOrElse(this.getStandardizedUuidAsString()) { UNKNOWN_UUID }
    return "$name - $this"
}

/**
 * Extension function that returns the [UUID] characteristic name for logging purposes.
 */
fun UUID.getCharacteristicName(): String {
    val name = gattCharacteristicUuids
        .getOrElse(this.getStandardizedUuidAsString()) { UNKNOWN_UUID }
    return "$name - $this"
}

/**
 * Extension function that returns the [UUID] descriptor name for logging purposes.
 */
fun UUID.getDescriptorName(): String {
    val name = gattDescriptorUuids
        .getOrElse(this.getStandardizedUuidAsString()) { UNKNOWN_UUID }
    return "$name - $this"
}

/**
 * Extension function that returns if the given [BleGattCharacteristic] has
 * [android.bluetooth.BluetoothGattCharacteristic.PROPERTY_BROADCAST] property.
 */
fun BleGattCharacteristic.isBroadcastable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_BROADCAST)

/**
 * Extension function that returns if the given [BleGattCharacteristic] has
 * [android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ] property.
 */
fun BleGattCharacteristic.isReadable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_READ)

/**
 * Extension function that returns if the given [BleGattCharacteristic] has
 * [android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE] property.
 */
fun BleGattCharacteristic.isWritableWithoutResponse(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

/**
 * Extension function that returns if the given [BleGattCharacteristic] has
 * [android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE] property.
 */
fun BleGattCharacteristic.isWritable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_WRITE)

/**
 * Extension function that returns if the given [BleGattCharacteristic] has
 * [android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY] property.
 */
fun BleGattCharacteristic.isNotifiable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_NOTIFY)

/**
 * Extension function that returns if the given [BleGattCharacteristic] has
 * [android.bluetooth.BluetoothGattCharacteristic.PROPERTY_INDICATE] property.
 */
fun BleGattCharacteristic.isIndicatable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_INDICATE)

private fun BleGattCharacteristic.containsProperty(property: Int): Boolean =
    properties and property != 0

/**
 * Extension function that returns the given [String] to bluetooth [String].
 */
fun String.toBluetoothUuidString(): String {
    require(this.length == UUID_16_BIT_LENGTH)
    return "0000$this-0000-1000-8000-00805F9B34FB"
}

/**
 * Extension function that returns the given [String] to bluetooth [UUID].
 */
fun String.toBluetoothUuid(): UUID {
    return UUID.fromString(this.toBluetoothUuidString())
}

/**
 * Extension function that returns the given [ByteArray] to hex [String] for logging purposes.
 */
fun ByteArray.toHexString(): String =
    joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }
