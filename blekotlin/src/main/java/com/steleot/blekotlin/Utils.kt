package com.steleot.blekotlin

import android.content.Context
import android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE
import java.util.Locale
import java.util.UUID

/**
 * Extension function for the [Context] that check if system feature for [FEATURE_BLUETOOTH_LE] is
 * supported.
 */
internal fun Context.isBleSupported() =
    packageManager.hasSystemFeature(FEATURE_BLUETOOTH_LE)

internal fun UUID.getStandardizedUuidAsString(): String {
    var result = ""
    val stringUUIDRepresentation: String = this.toString().toUpperCase(Locale.getDefault())
    if (stringUUIDRepresentation.startsWith("0000")
        && stringUUIDRepresentation.endsWith("-0000-1000-8000-00805F9B34FB")
    ) {
        result = stringUUIDRepresentation.substring(4, 8)
    }
    return result
}