@file:Suppress("ObjectPropertyName")

package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import com.steleot.blekotlin.internal.BleScanMode
import com.steleot.blekotlin.internal.CONNECT_DELAY
import com.steleot.blekotlin.internal.callback.BleDefaultGattCallback
import com.steleot.blekotlin.internal.callback.BleDefaultScanCallback
import com.steleot.blekotlin.internal.exception.BleException
import com.steleot.blekotlin.internal.helper.BlePermissionChecker
import com.steleot.blekotlin.internal.receiver.EmptyBleReceiver
import com.steleot.blekotlin.internal.utils.isBleSupported
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@SuppressLint("MissingPermission")
object BleClient : BleReceiver.BleReceiverListener,
    BleDefaultScanCallback.BleScanCallbackListener,
    BleDefaultGattCallback.BleGattCallbackListener {

    private const val TAG = "BleKotlin"

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var weakContext: WeakReference<Context>? = null
    private lateinit var bleLogger: BleLogger
    private var useBleReceiver = true
    private var bleAdapter: BleAdapter? = null
    private lateinit var permissionChecker: BlePermissionChecker
    private lateinit var bleReceiver: BroadcastReceiver
    private lateinit var bleDeviceStoreHelper: BleDeviceStoreHelper
    private var isScanning = false
        set(value) {
            if (!value) bleScanMode = BleScanMode.NoneMode
            field = value
        }
    private var bleScanMode: BleScanMode = BleScanMode.NoneMode
        set(value) {
            if (bleScanMode == BleScanMode.NoneMode) {
                _bleDevice.value = null
                _bleDevices.value = emptyList()
            }
            field = value
        }
    private var lastFilter: List<BleScanFilter>? = null
    private var lastSettings: BleScanSettings? = null
    private lateinit var bleScanCallback: BleScanCallback
    private var bleGatt: BleGatt? = null
    private lateinit var bleGattCallback: BleGattCallback

    private val _status: MutableStateFlow<BleStatus> = MutableStateFlow(BleStatus.NotStarted)
    val status = _status.asStateFlow()
    private val _bleDevice: MutableStateFlow<BleScanResult?> = MutableStateFlow(null)
    private val _bleDevices: MutableStateFlow<List<BleScanResult>> = MutableStateFlow(emptyList())

    fun init(
        context: Context,
        config: BleConfig = BleConfig(context)
    ) {
        /* from context */
        val applicationContext = context.applicationContext
        weakContext = WeakReference(applicationContext)
        val bluetoothManager = applicationContext
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bleAdapter = bluetoothManager?.adapter
        permissionChecker = BlePermissionChecker(applicationContext)
        /* from config */
        bleLogger = config.bleLogger
        useBleReceiver = config.bleReceiver !is EmptyBleReceiver
        bleReceiver = config.bleReceiver
        bleDeviceStoreHelper = config.bleDeviceStoreHelper
        bleScanCallback = BleDefaultScanCallback(bleLogger, this)
        bleGattCallback = BleDefaultGattCallback(bleLogger, this)
    }

    fun startBleScanSingle(
        filters: List<BleScanFilter>? = null,
        settings: BleScanSettings = BleScanSettingsBuilder().build()
    ): StateFlow<BleScanResult?> {
        startBleScanCommon(filters, settings, BleScanMode.SingleMode)
        return _bleDevice.asStateFlow()
    }

    fun startBleScanMultiple(
        filters: List<BleScanFilter>? = null,
        settings: BleScanSettings = BleScanSettingsBuilder().build()
    ): StateFlow<List<BleScanResult>> {
        startBleScanCommon(filters, settings, BleScanMode.ListMode)
        return _bleDevices.asStateFlow()
    }

    private fun startBleScanCommon(
        filters: List<BleScanFilter>?,
        settings: BleScanSettings,
        bleScanMode: BleScanMode
    ) {
        validateProperInitialization()
        if (isScanning) {
            bleLogger.log(TAG, "Ble is already scanning")
        } else {
            this.bleScanMode = bleScanMode
            startBleScanInternal(filters, settings)
        }
    }


    private fun validateProperInitialization() {
        // at least one late init property needed for the validation
        if (this::bleLogger.isInitialized) {
            return
        }
        throw BleException("Init was not called. Check documentation better.")
    }

    private fun startBleScanInternal(
        filters: List<BleScanFilter>?,
        settings: BleScanSettings
    ) {
        when {
            bleAdapter == null -> {
                bleLogger.log(TAG, "Bluetooth not available on device.")
                _status.value = BleStatus.BluetoothNotAvailable
            }
            weakContext?.get()?.isBleSupported() == false -> {
                bleLogger.log(TAG, "Bluetooth not supported.")
                _status.value = BleStatus.BleNotSupported
            }
            !permissionChecker.isBluetoothPermissionGranted() -> {
                bleLogger.log(TAG, "Bluetooth permission is not granted.")
                _status.value = BleStatus.BluetoothPermissionNotGranted
            }
            !bleAdapter!!.isEnabled -> {
                bleLogger.log(TAG, "Bluetooth is not enabled on device.")
                _status.value = BleStatus.BluetoothNotEnabled
            }
            !permissionChecker.isBluetoothAdminPermissionGranted() -> {
                bleLogger.log(TAG, "Bluetooth admin permission is not granted.")
                _status.value = BleStatus.BluetoothAdminPermissionNotGranted
            }
            !permissionChecker.isLocationPermissionGranted() -> {
                bleLogger.log(TAG, "Location permission is not granted.")
                _status.value = BleStatus.LocationPermissionNotGranted
            }
            else -> {
                bleLogger.log(TAG, "Ble scan started.")
                if (useBleReceiver) {
                    if (!isScanning) registerReceiver()
                    lastFilter = filters
                    lastSettings = settings
                }
                isScanning = true
                bleAdapter!!.bluetoothLeScanner!!.startScan(filters, settings, bleScanCallback)
            }
        }
    }

    private fun registerReceiver() {
        weakContext?.get()?.registerReceiver(
            bleReceiver, IntentFilter().apply {
                addAction(BleAdapter.ACTION_STATE_CHANGED)
                addAction(BleDevice.ACTION_BOND_STATE_CHANGED)
            }
        )
    }

    fun stopBleScan() {
        validateProperInitialization()
        isScanning = false
        lastFilter = null
        lastSettings = null
        if (useBleReceiver) unregisterReceiver()
        stopBleScanInternal()
    }

    private fun stopBleScanInternal() {
        bleLogger.log(TAG, "Stopping Ble scanning")
        bleAdapter!!.bluetoothLeScanner!!.stopScan(bleScanCallback)
    }

    private fun unregisterReceiver() {
        weakContext?.get()?.unregisterReceiver(bleReceiver)
    }

    fun connectTo(
        device: BleDevice
    ) {
        validateProperInitialization()
        bleGatt = null
        stopBleScanInternal()
        weakContext?.get()?.let { context ->
            coroutineScope.launch {
                delay(CONNECT_DELAY)
                /* autoconnect will not be supported */
                device.connectGatt(context, false, bleGattCallback)
            }
        }
    }

    override fun bluetoothStatus(
        isEnabled: Boolean
    ) {
        if (isEnabled) {
            bleLogger.log(TAG, "Bluetooth was enabled.")
            _status.value = BleStatus.BluetoothWasEnabled
            if (bleGatt != null) {
                bleLogger.log(TAG, "Trying to reconnect to ble device.")
                connectTo(bleGatt!!.device)
            } else if (isScanning) {
                bleLogger.log(TAG, "Trying to start ble scan again.")
                startBleScanInternal(lastFilter, lastSettings ?: BleScanSettingsBuilder().build())
            }
        } else {
            bleLogger.log(TAG, "Bluetooth was closed.")
            _status.value = BleStatus.BluetoothWasClosed
            stopBleScanInternal()
        }
    }

    override fun onScanResult(
        bleScanResult: BleScanResult?
    ) {
        if (bleScanMode == BleScanMode.SingleMode) {
            _bleDevice.value = bleScanResult
        } else if (bleScanMode == BleScanMode.ListMode) {
            if (bleScanResult != null) {
                val index = _bleDevices.value.indexOfFirst { result ->
                    result.device.address == bleScanResult.device.address
                }
                val list = _bleDevices.value.toMutableList()
                if (index != -1) {
                    list[index] = bleScanResult
                } else {
                    list.add(bleScanResult)
                }
                _bleDevices.value = list
                    .toList()
            } else {
                _bleDevices.value = listOf()
            }
        } else {
            bleLogger.log(TAG, "Something went wrong. Clearing all state flows.")
            _bleDevice.value = null
            _bleDevices.value = emptyList()
        }
    }

    override fun onGattSuccess(
        bleGatt: BleGatt
    ) {
        this.bleGatt = bleGatt
        coroutineScope.launch { bleGatt.discoverServices() }
    }

    override fun onGattFailure() {
        bleGatt?.close()
        bleGatt = null
        if (isScanning) {
            startBleScanInternal(
                lastFilter,
                lastSettings!!
            )
        }
    }
}
