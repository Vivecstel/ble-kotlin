package com.steleot.blekotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object BleKotlin {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var permissionChecker: PermissionChecker

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                // todo logger
            }
        }
    }

    private val _status: MutableStateFlow<BleStatus> = MutableStateFlow(BleStatus.NotStarted)
    val status = _status.asStateFlow()

    fun init(
        context: Context
    ) {
        val bluetoothManager = context
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter
        permissionChecker = PermissionChecker(context)
    }

    @SuppressLint("MissingPermission")
    fun startBleScan() {
        if (bluetoothAdapter == null) {
            _status.value = BleStatus.BluetoothNotAvailable
            return
        }

        if (!permissionChecker.isBluetoothPermissionGranted()) {
            _status.value = BleStatus.BluetoothPermissionNotGranted
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            _status.value = BleStatus.BluetoothNotEnabled
            return
        }

        if (!permissionChecker.isBluetoothAdminPermissionGranted()) {
            _status.value = BleStatus.BluetoothAdminPermissionNotGranted
            return
        }

        if (!permissionChecker.isLocationPermissionGranted()) {
            _status.value = BleStatus.LocationPermissionNotGranted
            return
        }

        bluetoothAdapter!!.bluetoothLeScanner!!.startScan(scanCallback)
    }
}
