package com.steleot.blekotlin

import android.content.Context
import android.content.pm.PackageManager

fun Context.isBleSupported() =
    packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
