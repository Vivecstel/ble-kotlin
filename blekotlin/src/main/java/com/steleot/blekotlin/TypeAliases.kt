package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings

/**
 * Type alias for [BluetoothManager].
 */
typealias BleManager = BluetoothManager

/**
 * Type alias for [BluetoothAdapter].
 */
typealias BleAdapter = BluetoothAdapter

/**
 * Type alias for [ScanFilter].
 */
typealias BleScanFilter = ScanFilter

/**
 * Type alias for [ScanFilter.Builder].
 */
typealias BleScanFilterBuilder = ScanFilter.Builder

/**
 * Type alias for [ScanSettings].
 */
typealias BleScanSettings = ScanSettings

/**
 * Type alias for [ScanSettings.Builder].
 */
typealias BleScanSettingsBuilder = ScanSettings.Builder

/**
 * Type alias for [ScanCallback].
 */
typealias BleScanCallback = ScanCallback

/**
 * Type alias for [ScanResult].
 */
typealias BleScanResult = ScanResult

/**
 * Type alias for [Pair] of [Int] manufacturer key and [ByteArray] manufacturer data.
 */
typealias BleManufacturerData = Pair<Int, ByteArray>

/**
 * Type alias for [BluetoothDevice].
 */
typealias BleDevice = BluetoothDevice

/**
 * Type alias for [BluetoothGatt].
 */
typealias BleGatt = BluetoothGatt

/**
 * Type alias for [BluetoothGattCallback].
 */
typealias BleGattCallback = BluetoothGattCallback

/**
 * Type alias for [BluetoothProfile].
 */
typealias BleProfile = BluetoothProfile

/**
 * Type alias for [BluetoothGattService].
 */
typealias BleGattService = BluetoothGattService

/**
 * Type alias for [BluetoothGattCharacteristic].
 */
typealias BleGattCharacteristic = BluetoothGattCharacteristic

/**
 * Type alias for [BluetoothGattDescriptor].
 */
typealias BleGattDescriptor = BluetoothGattDescriptor
