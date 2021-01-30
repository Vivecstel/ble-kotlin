package com.steleot.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleScanResult
import com.steleot.blekotlin.status.BleStatus
import com.steleot.sample.ui.utils.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _results = MutableLiveData<List<BleScanResultWithSelected>>()
    val results: LiveData<List<BleScanResultWithSelected>> = _results

    private val _bluetoothEnabled = MutableLiveData<Boolean>()
    val bluetoothEnabled: LiveData<Boolean> = _bluetoothEnabled

    private val _goToDetails = MutableLiveData<Event<BleScanResultWithSelected>>()
    val goToDetails: LiveData<Event<BleScanResultWithSelected>> = _goToDetails

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
                    is BleStatus.BluetoothNotEnabled -> {
                        Timber.d("Bluetooth not enabled")
                        _bluetoothEnabled.value = false
                    }
                    is BleStatus.LocationPermissionNotGranted ->
                        Timber.d("Location Permission not granted")
                    is BleStatus.BluetoothWasClosed -> {
                        Timber.d("Bluetooth was closed")
                        _bluetoothEnabled.value = false
                    }
                    is BleStatus.BluetoothWasEnabled -> {
                        Timber.d("Bluetooth was enabled")
                        _bluetoothEnabled.value = true
                    }
                }
            }
        }
    }

    fun startScanning() {
        viewModelScope.launch {
            BleClient.getStoredDevice().collect { device ->
                BleClient.startBleScanMultiple(
                    /* add filters if needed. The below is an example of heart rate search*/
                    /*listOf(
                        BleScanFilterBuilder()
                            .setServiceUuid(
                                ParcelUuid.fromString(BleGattServiceUuids.HEART_RATE.toBluetoothUuidString())
                            )
                            .build()
                    )*/
                ).collect { results ->
                    _results.value = results.map {
                        it to (it.device.address == device)
                    }.toList()
                }
            }
        }
    }

    fun stopScanning() {
        BleClient.stopBleScan()
    }

    fun handleDevice(
        item: BleScanResultWithSelected
    ) {
        _goToDetails.value = Event(item)
    }

    override fun onCleared() {
        stopScanning()
    }
}

typealias BleScanResultWithSelected = Pair<BleScanResult, Boolean>
