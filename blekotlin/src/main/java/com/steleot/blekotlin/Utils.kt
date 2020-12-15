package com.steleot.blekotlin

import android.content.Context
import android.content.pm.PackageManager

internal fun Context.isBleSupported() =
    packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
