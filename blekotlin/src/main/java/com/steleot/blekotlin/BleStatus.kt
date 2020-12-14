package com.steleot.blekotlin

sealed class BleStatus {

    object NotStarted : BleStatus()

    object BluetoothNotAvailable: BleStatus()

    object BluetoothPermissionNotGranted: BleStatus()

    object BluetoothAdminPermissionNotGranted: BleStatus()

    object BluetoothNotEnabled: BleStatus()

    object LocationPermissionNotGranted: BleStatus()
}