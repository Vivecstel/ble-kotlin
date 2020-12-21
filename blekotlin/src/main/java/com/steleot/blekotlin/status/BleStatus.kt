package com.steleot.blekotlin.status

/**
 * Sealed class for representation a bluetooth status.
 */
sealed class BleStatus {

    /**
     * Bluetooth status for not yet started.
     */
    object NotStarted : BleStatus()

    /**
     * Bluetooth status for bluetooth not available.
     */
    object BluetoothNotAvailable : BleStatus()

    /**
     * Bluetooth status for ble not supported.
     */
    object BleNotSupported : BleStatus()

    /**
     * Bluetooth status for bluetooth permission not granted.
     */
    object BluetoothPermissionNotGranted : BleStatus()

    /**
     * Bluetooth status for bluetooth not enabled on device.
     */
    object BluetoothNotEnabled : BleStatus()

    /**
     * Bluetooth status for bluetooth admin permission not granted.
     */
    object BluetoothAdminPermissionNotGranted : BleStatus()

    /**
     * Bluetooth status for location permission not granted.
     */
    object LocationPermissionNotGranted : BleStatus()

    /**
     * Bluetooth status for bluetooth was closed.
     */
    object BluetoothWasClosed : BleStatus()

    /**
     * Bluetooth status for bluetooth was enabled.
     */
    object BluetoothWasEnabled : BleStatus()
}