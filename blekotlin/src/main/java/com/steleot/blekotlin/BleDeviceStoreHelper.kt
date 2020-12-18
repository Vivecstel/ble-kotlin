package com.steleot.blekotlin

interface BleDeviceStoreHelper {

    fun saveBleDevice(
        key: String,
        address: String
    )

    fun getBleDevice(
        key: String
    ): String?

    fun deleteBleDevice(
        key: String
    )
}
