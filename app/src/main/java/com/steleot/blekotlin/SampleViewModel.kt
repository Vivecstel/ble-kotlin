package com.steleot.blekotlin

import android.bluetooth.le.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SampleViewModel: ViewModel() {

    private val _results = MutableLiveData<List<ScanResult>>()
    val results: LiveData<List<ScanResult>> = _results

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
            BleClient.startBleScan().collect { bleScanResult ->
                if (bleScanResult != null && bleScanResult.second == 0) {
                    val scanResult = bleScanResult.first
                    val index = _results.value!!.indexOfFirst { result ->
                        result.device.address == scanResult.device.address
                    }
                    val list = _results.value!!.toMutableList()
                    if (index != -1) {
                        list[index] = scanResult
                    } else {
                        list.add(scanResult)
                    }
                    _results.value = list
                        /* .sortedBy { it.rssi } */
                        .toList()
                } else {
                    _results.value = listOf()
                }
            }
        }
    }
}