package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * Logger tag constant.
 */
private const val TAG = "BleKotlin"

@SuppressLint("MissingPermission")
object BleClient : BleReceiver.BleReceiverCallbacks {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var weakContext: WeakReference<Context>? = null
    private lateinit var logger: BleLogger
    private var useBleReceiver = true
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var permissionChecker: BlePermissionChecker
    private lateinit var bleReceiver: BroadcastReceiver
    private var isScanning = false
    private var lastFilter: List<ScanFilter>? = null
    private var lastSettings: ScanSettings? = null
    private var bleGatt: BluetoothGatt? = null

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult
        ) {
            _bleDevice.value = result to 0
        }

        override fun onBatchScanResults(
            results: MutableList<ScanResult>?
        ) {
            /* empty implementation */
        }

        override fun onScanFailed(
            errorCode: Int
        ) {
            logger.log(
                TAG,
                "Error code $errorCode ${scanCallbackStatuses.getOrElse(errorCode) { UNKNOWN_ERROR }}"
            )
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            val deviceAddress = gatt.device.address
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    logger.log("BluetoothGattCallback", "Successfully connected to $deviceAddress")
                    bleGatt = gatt
                    coroutineScope.launch { gatt.discoverServices() }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    logger.log(
                        "BluetoothGattCallback",
                        "Successfully disconnected from $deviceAddress"
                    )
                    gatt.close()
                }
            } else {
                logger.log(
                    TAG, "Error $status ${gattStatuses.getOrElse(status) { UNKNOWN_STATUS }}" +
                            " encountered for $deviceAddress. Disconnecting device.."
                )
                gatt.close()
                if (isScanning) startBleScanInternal(lastFilter, lastSettings!!)
            }
        }

        override fun onServicesDiscovered(
            gatt: BluetoothGatt,
            status: Int
        ) {
            with(gatt) {
                logger.log(
                    "BluetoothGattCallback",
                    "Discovered ${services.size} services for ${device.address}"
                )
            }
        }
    }

    private val _status: MutableStateFlow<BleStatus> = MutableStateFlow(BleStatus.NotStarted)
    val status = _status.asStateFlow()
    private val _bleDevice: MutableStateFlow<BleScanResult?> = MutableStateFlow(null)

    fun init(
        context: Context,
        config: BleConfig = BleConfig()
    ) {
        val applicationContext = context.applicationContext
        weakContext = WeakReference(applicationContext)
        logger = config.logger
        useBleReceiver = config.useBleReceiver
        val bluetoothManager = context
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        permissionChecker = BlePermissionChecker(applicationContext)
    }

    fun startBleScan(
        filters: List<ScanFilter>? = null,
        settings: ScanSettings = ScanSettings.Builder().build()
    ): StateFlow<BleScanResult?> {
        if (isScanning) {
            logger.log(TAG, "Ble is already scanning")
        } else {
            startBleScanInternal(filters, settings)
        }
        return _bleDevice.asStateFlow()
    }

    private fun startBleScanInternal(
        filters: List<ScanFilter>?,
        settings: ScanSettings
    ) {
        when {
            bluetoothAdapter == null -> {
                logger.log(TAG, "Bluetooth not available on device.")
                _status.value = BleStatus.BluetoothNotAvailable
            }
            weakContext?.get()?.isBleSupported() == false -> {
                logger.log(TAG, "Bluetooth not supported.")
                _status.value = BleStatus.BleNotSupported
            }
            !permissionChecker.isBluetoothPermissionGranted() -> {
                logger.log(TAG, "Bluetooth permission is not granted.")
                _status.value = BleStatus.BluetoothPermissionNotGranted
            }
            !bluetoothAdapter!!.isEnabled -> {
                logger.log(TAG, "Bluetooth is not enabled on device.")
                _status.value = BleStatus.BluetoothNotEnabled
            }
            !permissionChecker.isBluetoothAdminPermissionGranted() -> {
                logger.log(TAG, "Bluetooth admin permission is not granted.")
                _status.value = BleStatus.BluetoothAdminPermissionNotGranted
            }
            !permissionChecker.isLocationPermissionGranted() -> {
                logger.log(TAG, "Location permission is not granted.")
                _status.value = BleStatus.LocationPermissionNotGranted
            }
            else -> {
                logger.log(TAG, "Ble scan started.")
                if (useBleReceiver) {
                    if (!isScanning) registerReceiver()
                    lastFilter = filters
                    lastSettings = settings
                }
                isScanning = true
                bluetoothAdapter!!.bluetoothLeScanner!!.startScan(filters, settings, scanCallback)
            }
        }
    }

    private fun registerReceiver() {
        bleReceiver = BleReceiver(logger, this)
        weakContext?.get()?.registerReceiver(
            bleReceiver, IntentFilter().apply {
                addAction(ACTION_STATE_CHANGED)
                addAction(ACTION_BOND_STATE_CHANGED)
            }
        )
    }

    fun stopBleScan() {
        isScanning = false
        lastFilter = null
        lastSettings = null
        if (useBleReceiver) unregisterReceiver()
        stopBleScanInternal()
    }

    private fun stopBleScanInternal() {
        logger.log(TAG, "Stopping Ble scanning")
        bluetoothAdapter!!.bluetoothLeScanner!!.stopScan(scanCallback)
    }

    private fun unregisterReceiver() {
        weakContext?.get()?.unregisterReceiver(bleReceiver)
    }

    override fun bluetoothStatus(
        isEnabled: Boolean
    ) {
        if (isEnabled) {
            logger.log(TAG, "Bluetooth was enabled.")
            _status.value = BleStatus.BluetoothWasEnabled
            if (isScanning) {
                startBleScanInternal(lastFilter, lastSettings!!)
            }
        } else {
            logger.log(TAG, "Bluetooth was closed.")
            _status.value = BleStatus.BluetoothWasClosed
            stopBleScanInternal()
        }
    }

    fun connectTo(
        device: BleDevice,
        autoConnect: Boolean = false
    ) {
        bleGatt = null
        stopBleScanInternal()
        weakContext?.get()?.let { context ->
            coroutineScope.launch {
                delay(CONNECT_DELAY)
                device.connectGatt(context, autoConnect, gattCallback)
            }
        }
    }
}
