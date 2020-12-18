package com.steleot.blekotlin

import android.content.Context
import com.steleot.blekotlin.internal.helper.BleDefaultDeviceStoreHelper
import com.steleot.blekotlin.internal.helper.BleDefaultLogger
import com.steleot.blekotlin.internal.receiver.DefaultBleReceiver

/**
 * Ble Configuration class with default values.
 * @param context: [Context] the context. Application context should be provided.
 * @param bleLogger: [BleLogger] for logging purposes. You can provide yours.
 * @param bleReceiver: [BleReceiver] for listening to bluetooth broadcasts. You can provide yours.
 * @param bleDeviceStoreHelper" [BleDeviceStoreHelper] for storing the ble device. You can provide yours.
 */
class BleConfig(
    val context: Context,
    val bleLogger: BleLogger = BleDefaultLogger(),
    val bleReceiver: BleReceiver = DefaultBleReceiver(bleLogger, BleClient),
    val bleDeviceStoreHelper: BleDeviceStoreHelper = BleDefaultDeviceStoreHelper(
        context.applicationContext,
        bleLogger
    )
)
