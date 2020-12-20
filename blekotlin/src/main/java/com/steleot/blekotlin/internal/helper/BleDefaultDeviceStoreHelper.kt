package com.steleot.blekotlin.internal.helper

import android.content.Context
import com.steleot.blekotlin.helper.BleDeviceStoreHelper
import com.steleot.blekotlin.helper.BleLogger
import com.steleot.blekotlin.internal.BLE_SHARED_PREFERENCES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val TAG = "BleDefaultDeviceStoreHelper"

internal class BleDefaultDeviceStoreHelper(
    context: Context,
    private val bleLogger: BleLogger
) : BleDeviceStoreHelper {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val sharedPreferences = context.getSharedPreferences(
        BLE_SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

    override suspend fun saveBleDevice(
        key: String,
        address: String
    ) {
        coroutineScope.launch {
            bleLogger.log(TAG, "Saving ble device with address $address.")
            with(sharedPreferences.edit()) {
                putString(key, address)
                apply()
            }
        }
    }

    override suspend fun getBleDevice(
        key: String
    ): Flow<String?> = flow {
        bleLogger.log(TAG, "Getting ble device from store.")
        emit(sharedPreferences.getString(key, null))
    }

    override suspend fun deleteBleDevice(
        key: String
    ) {
        coroutineScope.launch {
            bleLogger.log(TAG, "Deleting ble device from store.")
            with(sharedPreferences.edit()) {
                remove(key)
                apply()
            }
        }
    }
}