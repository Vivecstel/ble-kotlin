package com.steleot.blekotlin

import com.steleot.blekotlin.internal.BleDefaultLogger
import com.steleot.blekotlin.internal.receiver.DefaultBleReceiver

/**
 * Ble Configuration class with default values.
 * @param logger: [BleLogger] for logging purposes. You can provide yours.
 */
class BleConfig(
    val logger: BleLogger = BleDefaultLogger(),
    val bleReceiver: BleReceiver = DefaultBleReceiver(logger, BleClient)
)
