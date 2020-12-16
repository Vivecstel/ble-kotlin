package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter.*
import android.bluetooth.le.ScanCallback.*

val bluetoothStatuses by lazy {
    mapOf(
        STATE_OFF to "Bluetooth state off",
        STATE_TURNING_ON to "Bluetooth state turning on",
        STATE_ON to "Bluetooth state on",
        STATE_TURNING_OFF to "Bluetooth state turning off",
    )
}

val scanCallbackStatuses by lazy {
    mapOf(
        SCAN_FAILED_ALREADY_STARTED to "Ble scan failed - Already started",
        SCAN_FAILED_APPLICATION_REGISTRATION_FAILED to "Ble scan failed - Application registration failed",
        SCAN_FAILED_INTERNAL_ERROR to "Ble scan failed - Internal server error",
        SCAN_FAILED_FEATURE_UNSUPPORTED to "Ble scan failed - Feature unsupported",
    )
}