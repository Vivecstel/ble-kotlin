package com.steleot.blekotlin.internal.helper

import android.util.Log
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.BuildConfig

/**
 * The Default implementation for the [BleLogger].
 */
internal class BleDefaultLogger : BleLogger {

    override fun isEnabled(): Boolean = !BuildConfig.DEBUG

    override fun log(
        tag: String,
        message: String
    ) {
        if (isEnabled()) Log.d(tag, message)
    }
}
