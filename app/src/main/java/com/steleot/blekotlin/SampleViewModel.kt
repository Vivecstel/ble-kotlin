package com.steleot.blekotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SampleViewModel: ViewModel() {

    init {
        viewModelScope.launch {
            BleClient.status.collect { status ->
                when (status) {
                    is BleStatus.NotStarted ->
                        Timber.d("Not started")
                    is BleStatus.BluetoothNotAvailable ->
                        Timber.d("Bluetooth not available")
                    is BleStatus.BleNotSupported ->
                        Timber.d("Ble not supported")
                    is BleStatus.BluetoothPermissionNotGranted ->
                        Timber.d("Bluetooth permission not granted")
                    is BleStatus.BluetoothAdminPermissionNotGranted ->
                        Timber.d("Bluetooth admin permission not granted")
                    is BleStatus.BluetoothNotEnabled ->
                        Timber.d("Bluetooth not enabled")
                    is BleStatus.LocationPermissionNotGranted ->
                        Timber.d("Location Permission not granted")
                    is BleStatus.BluetoothWasClosed ->
                        Timber.d("Bluetooth was closed")
                    is BleStatus.BluetoothWasEnabled ->
                        Timber.d("Bluetooth was enabled")
                }
            }
        }
    }

    fun startScanning() {
        viewModelScope.launch {
            BleClient.startBleScan().collect {
                it?.let {
                    val device = it.first
                    Timber
                        .tag("STELIOS")
                        .d("BLE device with: name ${device.name}, address ${device.address} and rssi ${it.second}")
                }
            }
        }
    }
}