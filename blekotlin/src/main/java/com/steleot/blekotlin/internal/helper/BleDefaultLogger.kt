package com.steleot.blekotlin.internal.helper

import android.util.Log
import com.steleot.blekotlin.BleLogger

/**
 * The Default implementation for the [BleLogger].
 */
internal class BleDefaultLogger : BleLogger {

    override fun log(
        tag: String,
        message: String
    ) {
        Log.d(tag, message)
    }
}
