package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import com.steleot.blekotlin.internal.CONNECT_DELAY
import com.steleot.blekotlin.internal.callback.BleGattCallback
import com.steleot.blekotlin.internal.callback.BleScanCallback
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
    BleScanCallback.BleScanCallbackListener,
    BleGattCallback.BleGattCallbackListener {

    /**
     * Logger tag constant.
     */
    private const val TAG = "BleKotlin"

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var weakContext: WeakReference<Context>? = null
    private lateinit var logger: BleLogger
    private var useBleReceiver = true
    private var bluetoothAdapter: BleAdapter? = null
    private lateinit var permissionChecker: BlePermissionChecker
    private lateinit var bleReceiver: BroadcastReceiver
    private var isScanning = false
    private var lastFilter: List<ScanFilter>? = null
    private var lastSettings: ScanSettings? = null
    private lateinit var scanCallback: ScanCallback
    private var bleGatt: BleGatt? = null
    private lateinit var gattCallback: BluetoothGattCallback

    private val _status: MutableStateFlow<BleStatus> = MutableStateFlow(BleStatus.NotStarted)
    val status = _status.asStateFlow()
    private val _bleDevice: MutableStateFlow<BleScanResult?> = MutableStateFlow(null)

    fun init(
        context: Context,
        config: BleConfig = BleConfig()
    ) {
        /* from context */
        val applicationContext = context.applicationContext
        weakContext = WeakReference(applicationContext)
        val bluetoothManager = context
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        permissionChecker = BlePermissionChecker(applicationContext)
        /* from config */
        logger = config.logger
        useBleReceiver = config.bleReceiver !is EmptyBleReceiver
        bleReceiver = config.bleReceiver
        scanCallback = BleScanCallback(logger, this)
        gattCallback = BleGattCallback(logger, this)
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

    override fun onScanResult(
        scanResult: BleScanResult?
    ) {
        _bleDevice.value = scanResult
    }

    override fun onGattSuccess(
        gatt: BluetoothGatt
    ) {
        bleGatt = gatt
        coroutineScope.launch { gatt.discoverServices() }
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
