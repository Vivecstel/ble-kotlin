package com.steleot.sample.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleConnection
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleGattService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val bleDevice: BleDevice,
    private val isSaved: Boolean
) : ViewModel() {

    private lateinit var connection: BleConnection

    private val _connectionInfo = MutableLiveData(BleConnectionInfo())
    val connectionInfo: LiveData<BleConnectionInfo> = _connectionInfo

    init {
        viewModelScope.launch {
            connection = BleClient.connectTo(bleDevice)
            _connectionInfo.value = _connectionInfo.value?.copy(
                title = "${bleDevice.name} - ${bleDevice.address}",
                isSaved = isSaved
            )
            connection.status.collect { status ->
                if (status.isConnected) {
                    _connectionInfo.value = _connectionInfo.value?.copy(
                        connectionStatus = true,
                        services = connection.getServicesOnDevice() ?: emptyList()
                    )
                } else {
                    _connectionInfo.value = _connectionInfo.value?.copy(
                        connectionStatus = false,
                        services = emptyList()
                    )
                }
            }
        }
    }

    fun handleActionButton(
        isSaved: Boolean
    ) {
        viewModelScope.launch {
            if (isSaved) {
                BleClient.saveDevice(bleDevice)
            } else {
                BleClient.deleteStoredDevice()
            }
            _connectionInfo.value = _connectionInfo.value?.copy(
                isSaved = !isSaved
            )
        }
    }

    override fun onCleared() {
        connection.teardownConnection()
    }
}

data class BleConnectionInfo(
    val title: String = "",
    val isSaved: Boolean = false,
    val connectionStatus: Boolean = false,
    val services: List<BleGattService> = emptyList(),
)