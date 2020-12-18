package com.steleot.blekotlin.internal.helper

import android.content.Context
import com.steleot.blekotlin.BleDeviceStoreHelper
import com.steleot.blekotlin.BleLogger
import com.steleot.blekotlin.internal.BLE_SHARED_PREFERENCES

private const val TAG = "BleDefaultDeviceStoreHelper"

internal class BleDefaultDeviceStoreHelper(
    context: Context,
    private val bleLogger: BleLogger
) : BleDeviceStoreHelper {

    private val sharedPreferences = context.getSharedPreferences(
        BLE_SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

    override fun saveBleDevice(
        key: String,
        address: String
    ) {
        bleLogger.log(TAG, "Saving ble device with address $address.")
        with(sharedPreferences.edit()) {
            putString(key, address)
            apply()
        }
    }

    override fun getBleDevice(
        key: String
    ): String? {
        bleLogger.log(TAG, "Getting ble device from store.")
        return sharedPreferences.getString(key, null)
    }

    override fun deleteBleDevice(
        key: String
    ) {
        bleLogger.log(TAG, "Deleting ble device from store.")
        with(sharedPreferences.edit()) {
            remove(key)
            apply()
        }
    }
}