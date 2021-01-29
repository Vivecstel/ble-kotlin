package com.steleot.sample.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleConnection
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleGattService
import com.steleot.blekotlin.BleScanResult
import com.steleot.blekotlin.utils.getManufacturerData
import com.steleot.blekotlin.utils.getManufacturerName
import com.steleot.blekotlin.utils.toHexString
import com.steleot.sample.ui.utils.BleCharacteristicUtils.getDeviceName
import com.steleot.sample.ui.utils.BleCharacteristicUtils.getHeartRate
import com.steleot.sample.ui.utils.Event
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsViewModel(
    private val scanResult: BleScanResult,
    private val isSaved: Boolean
) : ViewModel() {

    private val bleDevice = scanResult.device
    private lateinit var connection: BleConnection

    private val _connectionInfo = MutableLiveData(BleConnectionInfo())
    val connectionInfo: LiveData<BleConnectionInfo> = _connectionInfo

    private val _text = MutableLiveData<Event<String>>()
    val text: LiveData<Event<String>> = _text

    private var serviceSet = mutableSetOf<UUID>()

    init {
        viewModelScope.launch {
            connection = BleClient.connectTo(bleDevice)
            _connectionInfo.value = _connectionInfo.value?.copy(
                title = "${bleDevice.name} - ${bleDevice.address}",
                isSaved = isSaved
            )
            connection.status.collect { status ->
                val manufacturerData = scanResult.getManufacturerData()
                val manufacturerName =
                    "Ble Manufacturer: ${manufacturerData?.first?.getManufacturerName() ?: "-"}"
                val manufacturerValue = "Value: ${manufacturerData?.second?.toHexString() ?: "-"}"
                if (status.isConnected) {

                    _connectionInfo.value = _connectionInfo.value?.copy(
                        connectionStatus = true,
                        manufacturerName = manufacturerName,
                        manufacturerValue = manufacturerValue,
                        services = connection.getServicesOnDevice(bleDevice)
                    )
                } else {
                    _connectionInfo.value = _connectionInfo.value?.copy(
                        connectionStatus = false,
                        manufacturerName = manufacturerName,
                        manufacturerValue = manufacturerValue,
                        services = emptyList(),
                    )
                }
                status.bleGattCharacteristic?.handleAction()
            }
        }
    }

    private fun BleGattCharacteristic.handleAction() {
        if (this.value == null) return
        val deviceName = this.getDeviceName()
        val heartRate = this.getHeartRate()
        val actualValue: String = when {
            deviceName != null -> deviceName
            heartRate != null -> heartRate.toString()
            else -> this.value.toHexString()
        }
        val text = "${this.uuid} with: $actualValue"
        _text.value = Event(text)
        Timber.d(text)
    }

    fun handleActionButton(
        isSaved: Boolean
    ) {
        viewModelScope.launch {
            if (isSaved) {
                BleClient.deleteStoredDevice()
            } else {
                BleClient.saveDevice(bleDevice)
            }
            _connectionInfo.value = _connectionInfo.value?.copy(
                isSaved = !isSaved
            )
        }
    }

    fun handleReadAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Reading the value of characteristic.")
        connection.readCharacteristic(bleDevice, bleGattCharacteristic.uuid)
    }

    fun handleWriteAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Writing a random value to characteristic.")
        connection.writeCharacteristic(
            bleDevice, bleGattCharacteristic.uuid, Random.nextBytes(ByteArray(2))
        )
    }

    fun handleNotifiableAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Enabling / Disabling notification for characteristic.")
        val uuid = bleGattCharacteristic.uuid
        if (serviceSet.contains(uuid)) {
            serviceSet.remove(uuid)
            connection.disableNotifications(bleDevice, uuid)
        } else {
            serviceSet.add(uuid)
            connection.enableNotifications(bleDevice, uuid)
        }
    }

    override fun onCleared() {
        connection.teardownConnection(bleDevice)
    }
}

data class BleConnectionInfo(
    val title: String = "",
    val isSaved: Boolean = false,
    val connectionStatus: Boolean = false,
    val manufacturerName: String = "",
    val manufacturerValue: String = "",
    val services: List<BleGattService> = emptyList(),
)
