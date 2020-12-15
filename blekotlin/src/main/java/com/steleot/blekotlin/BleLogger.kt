package com.steleot.blekotlin

import android.util.Log

interface BleLogger {

    fun log(
            tag: String,
            message: String
    )
}

internal class DefaultBleLogger: BleLogger {

    override fun log(
            tag: String,
            message: String
    ) {
        Log.d(tag, message)
    }
}