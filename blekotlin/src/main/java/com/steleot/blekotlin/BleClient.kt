package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.ref.WeakReference

private const val TAG = "BleKotlin"

object BleClient {

    private var weakContext: WeakReference<Context>? = null
    private lateinit var logger: BleLogger
    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var permissionChecker: BlePermissionChecker

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult
        ) {
            with(result.device) {
                logger.log(
                    TAG,
                    "BLE device with address : ${this.address} with rssi : ${result.rssi}"
                )
            }
        }

        override fun onBatchScanResults(
            results: MutableList<ScanResult>?
        ) {
            /* empty implementation */
        }

        override fun onScanFailed(
            errorCode: Int
        ) {
            // todo
        }
    }

    private val _status: MutableStateFlow<BleStatus> = MutableStateFlow(BleStatus.NotStarted)
    val status = _status.asStateFlow()

    fun init(
        context: Context,
        config: BleConfig = BleConfig()
    ) {
        val applicationContext = context.applicationContext
        weakContext = WeakReference(applicationContext)
        logger = config.logger
        val bluetoothManager = context
                .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        permissionChecker = BlePermissionChecker(applicationContext)
    }

    @SuppressLint("MissingPermission")
    fun startBleScan(
        filters: List<ScanFilter>? = null,
        settings: ScanSettings? = ScanSettings.Builder().build()
    ) {
        if (bluetoothAdapter == null) {
            logger.log(TAG, "Bluetooth not available on device.")
            _status.value = BleStatus.BluetoothNotAvailable
            return
        }

        if (weakContext?.get()?.isBleSupported() == false) {
            logger.log(TAG, "Bluetooth not supported.")
            _status.value = BleStatus.BleNotSupported
            return
        }

        if (!permissionChecker.isBluetoothPermissionGranted()) {
            logger.log(TAG, "Bluetooth permission is not granted.")
            _status.value = BleStatus.BluetoothPermissionNotGranted
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            logger.log(TAG, "Bluetooth is not enabled on device.")
            _status.value = BleStatus.BluetoothNotEnabled
            return
        }

        if (!permissionChecker.isBluetoothAdminPermissionGranted()) {
            logger.log(TAG, "Bluetooth admin permission is not granted.")
            _status.value = BleStatus.BluetoothAdminPermissionNotGranted
            return
        }

        if (!permissionChecker.isLocationPermissionGranted()) {
            logger.log(TAG, "Location permission is not granted.")
            _status.value = BleStatus.LocationPermissionNotGranted
            return
        }

        logger.log(TAG, "Ble scan started.")
        bluetoothAdapter!!.bluetoothLeScanner!!.startScan(filters, settings, scanCallback)
    }
}
