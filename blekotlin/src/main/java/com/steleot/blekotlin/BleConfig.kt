package com.steleot.blekotlin

/**
 * Ble Configuration class with default values.
 * @param logger: [BleLogger] for logging purposes. You can provide yours.
 * @param useBleReceiver: [Boolean] for registering the ble broadcast receiver for bluetooth.
 */
class BleConfig(
    val logger: BleLogger = DefaultBleLogger(),
    val useBleReceiver: Boolean = true
)
