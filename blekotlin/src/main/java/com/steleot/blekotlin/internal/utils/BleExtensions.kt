package com.steleot.blekotlin.internal.utils

import android.content.Context
import android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleGattDescriptor
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.BleDescriptors
import com.steleot.blekotlin.internal.UNKNOWN_ERROR
import com.steleot.blekotlin.internal.UNKNOWN_STATE
import com.steleot.blekotlin.internal.UNKNOWN_STATUS
import com.steleot.blekotlin.internal.UNKNOWN_UUID
import com.steleot.blekotlin.internal.UUID_16_BIT_LENGTH
import com.steleot.blekotlin.internal.UUID_END_INDEX
import com.steleot.blekotlin.internal.UUID_START_INDEX
import java.util.Locale
import java.util.UUID

/**
 * Extension function for the [Context] that check if system feature for [FEATURE_BLUETOOTH_LE] is
 * supported.
 */
internal fun Context.isBleSupported() =
    packageManager.hasSystemFeature(FEATURE_BLUETOOTH_LE)

internal fun Int.getBleState(): String {
    return "${bleStatuses.getOrElse(this) { UNKNOWN_STATUS }} - $this"
}

internal fun Int.getBleBondState(): String {
    return "${bleBondStates.getOrElse(this) { UNKNOWN_STATE }} - $this"
}

internal fun Int.getBleScanCallbackStatus(): String {
    return "${scanCallbackStatuses.getOrElse(this) { UNKNOWN_ERROR }} - $this"
}

internal fun Int.getBleGattStatus(): String {
    return "${gattStatuses.getOrElse(this) { UNKNOWN_ERROR }} - $this"
}

internal fun BleGatt.findCharacteristic(
    characteristicUuid: UUID
): BleGattCharacteristic? {
    services?.forEach { service ->
        service.characteristics?.firstOrNull { characteristic ->
            characteristic.uuid == characteristicUuid
        }?.let { matchingCharacteristic ->
            return matchingCharacteristic
        }
    }
    return null
}

internal fun BleGatt.findDescriptor(uuid: UUID): BleGattDescriptor? {
    services?.forEach { service ->
        service.characteristics.forEach { characteristic ->
            characteristic.descriptors?.firstOrNull { descriptor ->
                descriptor.uuid == uuid
            }?.let { matchingDescriptor ->
                return matchingDescriptor
            }
        }
    }
    return null
}

internal fun BleGatt.printGattInformation(
    bleLogger: BleLogger,
    tag: String
) {
    if (!bleLogger.isEnabled()) return

    if (services.isEmpty()) {
        bleLogger.log(
            tag, "No service and characteristic available. Call " +
                    "discoverServices first."
        )
        return
    }

    services.forEach { service ->
        val characteristicsTable = service.characteristics.joinToString(
            separator = "\n|--",
            prefix = "|--"
        ) { characteristic ->
            var description = "${characteristic.uuid.getCharacteristicName()}:" +
                    " ${characteristic.printProperties()}"
            if (characteristic.descriptors.isNotEmpty()) {
                description += "\n" + characteristic.descriptors.joinToString(
                    separator = "\n|------",
                    prefix = "|------"
                ) { descriptor ->
                    "${descriptor.uuid.getDescriptorName()}: ${descriptor.printProperties()}"
                }
            }
            description
        }
        bleLogger.log(
            tag, "Service ${service.uuid.getServiceName()}\n" +
                    "Characteristics:\n$characteristicsTable"
        )
    }
}

internal fun UUID.getServiceName(): String {
    val name = gattServicesUuids
        .getOrElse(this.getStandardizedUuidAsString()) { UNKNOWN_UUID }
    return "$name - $this"
}

internal fun UUID.getCharacteristicName(): String {
    val name = gattCharacteristicUuids
        .getOrElse(this.getStandardizedUuidAsString()) { UNKNOWN_UUID }
    return "$name - $this"
}

internal fun UUID.getDescriptorName(): String {
    val name = gattDescriptorUuids
        .getOrElse(this.getStandardizedUuidAsString()) { UNKNOWN_UUID }
    return "$name - $this"
}

private fun BleGattCharacteristic.printProperties(): String = mutableListOf<String>().apply {
    if (isBroadcastable()) add("broadcastable")
    if (isReadable()) add("readable")
    if (isWritableWithoutResponse()) add("writable without response")
    if (isWritable()) add("writable")
    if (isNotifiable()) add("notifiable")
    if (isIndicatable()) add("indicatable")
    if (isEmpty()) add("empty")
}.joinToString()

internal fun BleGattCharacteristic.isBroadcastable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_BROADCAST)

internal fun BleGattCharacteristic.isReadable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_READ)

internal fun BleGattCharacteristic.isWritableWithoutResponse(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

internal fun BleGattCharacteristic.isWritable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_WRITE)

internal fun BleGattCharacteristic.isNotifiable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_NOTIFY)

internal fun BleGattCharacteristic.isIndicatable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_INDICATE)

private fun BleGattCharacteristic.containsProperty(property: Int): Boolean =
    properties and property != 0

private fun BleGattDescriptor.printProperties(): String = mutableListOf<String>().apply {
    if (isReadable()) add("readable")
    if (isWritable()) add("writable")
    if (isEmpty()) add("empty")
}.joinToString()

internal fun BleGattDescriptor.isReadable(): Boolean =
    containsPermission(BleGattDescriptor.PERMISSION_READ)

internal fun BleGattDescriptor.isWritable(): Boolean =
    containsPermission(BleGattDescriptor.PERMISSION_WRITE)

private fun BleGattDescriptor.containsPermission(permission: Int): Boolean =
    permissions and permission != 0

private fun UUID.getStandardizedUuidAsString(): String {
    var result = ""
    val stringUUIDRepresentation: String = this.toString().toUpperCase(Locale.getDefault())
    if (stringUUIDRepresentation.startsWith("0000")
        && stringUUIDRepresentation.endsWith("-0000-1000-8000-00805F9B34FB")
    ) {
        result = stringUUIDRepresentation.substring(UUID_START_INDEX, UUID_END_INDEX)
    }
    return result
}

internal fun String.toBluetoothUuid(): UUID {
    return UUID.fromString(this.toBluetoothUuidString())
}

internal fun String.toBluetoothUuidString(): String {
    require(this.length == UUID_16_BIT_LENGTH)
    return "0000$this-0000-1000-8000-00805F9B34FB"
}

internal fun ByteArray.toHexString(): String =
    joinToString(separator = " ", prefix = "0x") { String.format("%02X", it) }

internal fun BleGattDescriptor.isClientCharacteristicConfigurationDescriptor(): Boolean {
    return BleDescriptors.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuidString()
        .equals(uuid.toString(), ignoreCase = true)
}
