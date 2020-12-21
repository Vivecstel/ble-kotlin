package com.steleot.sample.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleGattService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val bleDevice: BleDevice
)  : ViewModel() {

    private val _connectionStatus = MutableLiveData<String>()
    val connectionStatus: LiveData<String> = _connectionStatus

    private val _services = MutableLiveData<List<BleGattService>>()
    val services: LiveData<List<BleGattService>> = _services

    init {
        viewModelScope.launch {
            val connection = BleClient.connectTo(bleDevice)
            connection.status.collect { status ->
                if (status.isConnected) {
                    _connectionStatus.value = "Connected"
                    _services.value = connection.getServicesOnDevice()
                } else {
                    _connectionStatus.value = "Not Connected"
                    _services.value = emptyList()
                }
            }
        }
    }
}