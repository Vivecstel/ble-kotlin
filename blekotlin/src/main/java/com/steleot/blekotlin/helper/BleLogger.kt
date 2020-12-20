package com.steleot.blekotlin.helper

/**
 * Default interface for logging.
 */
interface BleLogger {

    /**
     * Used for to check if logging is enabled. The default value is [true]. Override this for
     * custom behavior.
     */
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
