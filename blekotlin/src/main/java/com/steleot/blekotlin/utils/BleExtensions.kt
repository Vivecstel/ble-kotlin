package com.steleot.blekotlin.utils

import com.steleot.blekotlin.internal.UNKNOWN_UUID
import com.steleot.blekotlin.internal.utils.gattCharacteristicUuids
import com.steleot.blekotlin.internal.utils.gattDescriptorUuids
import com.steleot.blekotlin.internal.utils.gattServicesUuids
import com.steleot.blekotlin.internal.utils.getStandardizedUuidAsString
import java.util.UUID

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
