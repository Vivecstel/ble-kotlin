package com.steleot.sample.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steleot.blekotlin.BleClient
import com.steleot.blekotlin.BleConnection
import com.steleot.blekotlin.BleDevice
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.BleGattService
import com.steleot.blekotlin.utils.toHexString
import kotlin.random.Random
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsViewModel(
    private val bleDevice: BleDevice,
    private val isSaved: Boolean
) : ViewModel() {

    private lateinit var connection: BleConnection

    private val _connectionInfo = MutableLiveData(BleConnectionInfo())
    val connectionInfo: LiveData<BleConnectionInfo> = _connectionInfo

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private var lastAction = Action.NONE

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
                status.bleGattCharacteristic?.let {
                    when (lastAction) {
                        Action.READ ->
                            _text.value = "Read action: ${it.uuid} with: ${it.value.toHexString()}"
                        Action.WRITE ->
                            _text.value = "Write action: ${it.uuid} with: ${it.value.toHexString()}"
                        Action.NOTIFY ->
                            _text.value =
                                "Notify action: ${it.uuid} with: ${it.value.toHexString()}"
                        else -> _text.value = ""
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
        connection.readCharacteristic(bleGattCharacteristic.uuid)
    }

    fun handleWriteAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Writing a random value to characteristic.")
        lastAction = Action.WRITE
        connection.writeCharacteristic(
            bleGattCharacteristic.uuid, Random.nextBytes(ByteArray(4))
        )
    }

    fun handleNotifiableAction(
        bleGattCharacteristic: BleGattCharacteristic
    ) {
        Timber.d("Creating notification for characteristic.")
        lastAction = Action.NOTIFY
        connection.enableNotifications(bleGattCharacteristic.uuid)
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

enum class Action {
    NONE, READ, WRITE, NOTIFY
}