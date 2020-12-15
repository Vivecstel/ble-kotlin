package com.steleot.blekotlin

class BleConfig(
    val logger: BleLogger = DefaultBleLogger(),
    val useBleReceiver: Boolean = true
)
