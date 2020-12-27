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

    private var lastAction = Action.NONE

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
                status.bleGattCharacteristic?.let {
                    when (lastAction) {
                        Action.READ -> {
                            val deviceName = it.getDeviceName()
                            val heartRate = it.getHeartRate()
                            val value: String = when {
                                deviceName != null -> deviceName
                                heartRate != null -> heartRate.toString()
                                else -> it.value.toHexString()
                            }
                            _text.value = Event("Read action: ${it.uuid} with: $value")
                        }
                        Action.WRITE -> _text.value = Event(
                            "Write action: ${it.uuid} with: ${it.value.toHexString()}"
                        )
                        Action.NOTIFY -> _text.value = Event(
                            "Notify action: ${it.uuid} with: ${it.value.toHexString()}"
                        )
                        else -> _text.value = Event("")
                    }
                }
            }
        }
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
        lastAction = Action.READ
        connection.readCharacteristic(bleDevice, bleGattCharacteristic.uuid)
    }

    fun handleWriteAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Writing a random value to characteristic.")
        lastAction = Action.WRITE
        connection.writeCharacteristic(
            bleDevice, bleGattCharacteristic.uuid, Random.nextBytes(ByteArray(2))
        )
    }

    fun handleNotifiableAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Creating notification for characteristic.")
        lastAction = Action.NOTIFY
        connection.enableNotifications(bleDevice, bleGattCharacteristic.uuid)
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

enum class Action {
    NONE, READ, WRITE, NOTIFY
}
