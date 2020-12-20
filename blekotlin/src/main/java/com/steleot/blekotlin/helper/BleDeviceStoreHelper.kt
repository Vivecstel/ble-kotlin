package com.steleot.blekotlin.helper

import kotlinx.coroutines.flow.Flow

/**
 * The ble device store helper. You can implement this to use different device storage. The default
 * one uses shared preferences.
 */
interface BleDeviceStoreHelper {

    /**
     * Save a ble device.
     * @param key: [String] the key for saving the ble device.
     * @param address: [String] the mac address of the ble device.
     */
    suspend fun saveBleDevice(
        key: String,
        address: String
    )

    /**
     * Gets the ble device max address as [Flow] of String.
     * @param key: [String] the key for getting the ble device.
     */
    suspend fun getBleDevice(
        key: String
    ): Flow<String?>

    /**
     * Deletes the ble device if it exists.
     * @param key: [String] the key for deleting the ble device.
     */
    suspend fun deleteBleDevice(
        key: String
    )
}
