package com.steleot.blekotlin

import android.util.Log

/**
 * Default interface for logging.
 */
interface BleLogger {

    /**
     * Function that prints a given message for the specific tag.
     * @param tag: The tag given, mostly a constant that represents a class name.
     * @param message: The message given.
     */
    fun log(
        tag: String,
        message: String
    )
}

/**
 * The Default implementation for the [BleLogger].
 */
internal class DefaultBleLogger : BleLogger {

    override fun log(
        tag: String,
        message: String
    ) {
        Log.d(tag, message)
    }
}