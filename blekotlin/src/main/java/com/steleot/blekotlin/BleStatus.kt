package com.steleot.blekotlin

sealed class BleStatus {

    class NotStarted: BleStatus()

    class BluetoothNotAvailable: BleStatus()

    class LocationPermissionNotGranted: BleStatus()

    class BluetoothNotEnabled: BleStatus()

    class LocationServicesNotEnabled: BleStatus()

    class Ready: BleStatus()
}