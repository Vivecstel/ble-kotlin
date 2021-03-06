package com.steleot.blekotlin.internal.utils

import android.content.Context
import android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE
import com.steleot.blekotlin.BleGatt
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleGattDescriptor
import com.steleot.blekotlin.constants.BleGattDescriptorUuids
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.UNKNOWN_ERROR
import com.steleot.blekotlin.internal.UNKNOWN_STATE
import com.steleot.blekotlin.internal.UNKNOWN_STATUS
import com.steleot.blekotlin.internal.UUID_END_INDEX
import com.steleot.blekotlin.internal.UUID_START_INDEX
import com.steleot.blekotlin.utils.getCharacteristicName
import com.steleot.blekotlin.utils.getDescriptorName
import com.steleot.blekotlin.utils.getServiceName
import com.steleot.blekotlin.utils.isBroadcastable
import com.steleot.blekotlin.utils.isIndicatable
import com.steleot.blekotlin.utils.isNotifiable
import com.steleot.blekotlin.utils.isReadable
import com.steleot.blekotlin.utils.isWritable
import com.steleot.blekotlin.utils.isWritableWithoutResponse
import com.steleot.blekotlin.utils.toBluetoothUuidString
import java.util.Locale
import java.util.UUID

/**
 * Extension function for the [Context] that check if system feature for [FEATURE_BLUETOOTH_LE] is
 * supported.
 */
internal fun Context.isBleSupported() =
    packageManager.hasSystemFeature(FEATURE_BLUETOOTH_LE)

/**
 * Extension function that returns the bluetooth state for logging purposes.
 */
internal fun Int.getBleState(): String {
    return "${bleStatuses.getOrElse(this) { UNKNOWN_STATUS }} - $this"
}

/**
 * Extension function that returns the bluetooth device bond state for logging purposes.
 */
internal fun Int.getBleBondState(): String {
    return "${bleBondStates.getOrElse(this) { UNKNOWN_STATE }} - $this"
}

/**
 * Extension function that returns the bluetooth scan callback status for logging purposes.
 */
internal fun Int.getBleScanCallbackStatus(): String {
    return "${scanCallbackStatuses.getOrElse(this) { UNKNOWN_ERROR }} - $this"
}

/**
 * Extension function that returns the bluetooth gatt status for logging purposes.
 */
internal fun Int.getBleGattStatus(): String {
    return "${gattStatuses.getOrElse(this) { UNKNOWN_ERROR }} - $this"
}

/**
 * Extension function that find characteristic of the [BleGatt] for the given [UUID].
 * @param characteristicUuid: [UUID] the given uuid of the characteristic.
 */
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

/**
 * Extension function that find descriptor of the [BleGatt] for the given [UUID].
 * @param descriptorUuid: [UUID] the given uuid of the descriptor.
 */
internal fun BleGatt.findDescriptor(
    descriptorUuid: UUID
): BleGattDescriptor? {
    services?.forEach { service ->
        service.characteristics.forEach { characteristic ->
            characteristic.descriptors?.firstOrNull { descriptor ->
                descriptor.uuid == descriptorUuid
            }?.let { matchingDescriptor ->
                return matchingDescriptor
            }
        }
    }
    return null
}

/**
 * Extension function that logs all services / characteristics / descriptor for the given [BleGatt].
 * @param bleLogger: [BleLogger] instance.
 * @param tag: [String] the given tag for logging.
 */
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

private fun BleGattCharacteristic.printProperties(): String = mutableListOf<String>().apply {
    if (isBroadcastable()) add("broadcastable")
    if (isReadable()) add("readable")
    if (isWritableWithoutResponse()) add("writable without response")
    if (isWritable()) add("writable")
    if (isNotifiable()) add("notifiable")
    if (isIndicatable()) add("indicatable")
    if (isEmpty()) add("empty")
}.joinToString()

private fun BleGattDescriptor.printProperties(): String = mutableListOf<String>().apply {
    if (isReadable()) add("readable")
    if (isWritable()) add("writable")
    if (isEmpty()) add("empty")
}.joinToString()

/**
 * Extension function that returns if the given [BleGattDescriptor] has
 * [android.bluetooth.BluetoothGattDescriptor.PERMISSION_READ] permission.
 */
internal fun BleGattDescriptor.isReadable(): Boolean =
    containsPermission(BleGattDescriptor.PERMISSION_READ)

/**
 * Extension function that returns if the given [BleGattDescriptor] has
 * [android.bluetooth.BluetoothGattDescriptor.PERMISSION_WRITE] permission.
 */
internal fun BleGattDescriptor.isWritable(): Boolean =
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

/**
 * Extension function that checks if the given [BleGattDescriptor] is of
 * [BleGattDescriptorUuids.CLIENT_CHARACTERISTIC_CONFIGURATION] that is needed for enabling / disabling
 * notification for [BleGattCharacteristic].
 */
internal fun BleGattDescriptor.isClientCharacteristicConfigurationDescriptor(): Boolean {
    return BleGattDescriptorUuids.CLIENT_CHARACTERISTIC_CONFIGURATION.toBluetoothUuidString()
        .equals(uuid.toString(), ignoreCase = true)
}
