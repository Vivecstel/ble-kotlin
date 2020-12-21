package com.steleot.blekotlin.internal.exception

/**
 * A custom [RuntimeException] that is thrown when [com.steleot.blekotlin.BleClient] is not
 * initialized correctly.
 */
internal class BleException(
    message: String
) : RuntimeException(message)
