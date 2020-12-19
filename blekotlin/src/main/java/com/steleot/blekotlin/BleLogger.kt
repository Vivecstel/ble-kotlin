package com.steleot.blekotlin

/**
 * Default interface for logging.
 */
interface BleLogger {

    fun isEnabled(): Boolean = true

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
