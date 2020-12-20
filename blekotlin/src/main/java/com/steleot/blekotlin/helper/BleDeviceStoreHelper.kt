package com.steleot.blekotlin.helper

import kotlinx.coroutines.flow.Flow

interface BleDeviceStoreHelper {

    suspend fun saveBleDevice(
        key: String,
        address: String
    )

    suspend fun getBleDevice(
        key: String
    ): Flow<String?>

    suspend fun deleteBleDevice(
        key: String
    )
}
