package com.steleot.blekotlin.internal.utils

import android.content.Context
import android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleGattDescriptor
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.internal.UNKNOWN_UUID
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

fun BleGatt.printGattInformation(
    bleLogger: BleLogger,
    tag: String
) {
    if (!bleLogger.isEnabled()) return

    if (services.isEmpty()) {
        bleLogger.log(tag, "No service and characteristic available. Call " +
                "discoverServices first.")
        return
    }

    services.forEach { service ->
        val characteristicsTable = service.characteristics.joinToString(
            separator = "\n|--",
            prefix = "|--"
        ) { characteristic ->
            val charDescription = gattCharacteristicUuids
                .getOrElse(characteristic.uuid.getStandardizedUuidAsString()) { UNKNOWN_UUID }
            var description = "$charDescription - ${characteristic.uuid}:" +
                    " ${characteristic.printProperties()}"
            if (characteristic.descriptors.isNotEmpty()) {
                description += "\n" + characteristic.descriptors.joinToString(
                    separator = "\n|------",
                    prefix = "|------"
                ) { descriptor ->
                    val descriptorDescription = gattDescriptorUuids
                        .getOrElse(descriptor.uuid.getStandardizedUuidAsString()) { UNKNOWN_UUID }
                    "$descriptorDescription-${descriptor.uuid}: ${descriptor.printProperties()}"
                }
            }
            description
        }
        val serviceDescription = gattServicesUuids
            .getOrElse(service.uuid.getStandardizedUuidAsString()) { UNKNOWN_UUID }
        bleLogger.log(tag, "Service $serviceDescription-${service.uuid}\nCharacteristics:" +
                "\n$characteristicsTable")
    }
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

private fun BleGattCharacteristic.isBroadcastable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_BROADCAST)

private fun BleGattCharacteristic.isReadable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_READ)

private fun BleGattCharacteristic.isWritableWithoutResponse(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

private fun BleGattCharacteristic.isWritable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_WRITE)

private fun BleGattCharacteristic.isNotifiable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_NOTIFY)

private fun BleGattCharacteristic.isIndicatable(): Boolean =
    containsProperty(BleGattCharacteristic.PROPERTY_INDICATE)

private fun BleGattCharacteristic.containsProperty(property: Int): Boolean =
    properties and property != 0

private fun BleGattDescriptor.printProperties(): String = mutableListOf<String>().apply {
    if (isReadable()) add("readable")
    if (isWritable()) add("writable")
    if (isEmpty()) add("empty")
}.joinToString()

private fun BleGattDescriptor.isReadable(): Boolean =
    containsPermission(BleGattDescriptor.PERMISSION_READ)

private fun BleGattDescriptor.isWritable(): Boolean =
    containsPermission(BleGattDescriptor.PERMISSION_WRITE)

private fun BleGattDescriptor.containsPermission(permission: Int): Boolean =
    permissions and permission != 0

internal fun UUID.getStandardizedUuidAsString(): String {
    var result = ""
    val stringUUIDRepresentation: String = this.toString().toUpperCase(Locale.getDefault())
    if (stringUUIDRepresentation.startsWith("0000")
        && stringUUIDRepresentation.endsWith("-0000-1000-8000-00805F9B34FB")
    ) {
        result = stringUUIDRepresentation.substring(UUID_START_INDEX, UUID_END_INDEX)
    }
    return result
}
