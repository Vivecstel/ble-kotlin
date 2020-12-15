package com.steleot.blekotlin

sealed class BleStatus {

    object NotStarted : BleStatus()

    object BluetoothNotAvailable: BleStatus()

    object BleNotSupported: BleStatus()

    object BluetoothPermissionNotGranted: BleStatus()

    object BluetoothNotEnabled: BleStatus()

    object BluetoothAdminPermissionNotGranted: BleStatus()

    object LocationPermissionNotGranted: BleStatus()
}