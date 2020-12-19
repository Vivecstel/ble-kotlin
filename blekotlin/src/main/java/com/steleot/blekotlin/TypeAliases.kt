package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings

typealias BleManager = BluetoothManager

typealias BleAdapter = BluetoothAdapter

typealias BleScanFilter = ScanFilter

typealias BleScanSettings = ScanSettings

typealias BleScanSettingsBuilder = ScanSettings.Builder

typealias BleScanCallback = ScanCallback

typealias BleScanResult = ScanResult

typealias BleDevice = BluetoothDevice

typealias BleGatt = BluetoothGatt

typealias BleGattCallback = BluetoothGattCallback

typealias BleProfile = BluetoothProfile

typealias BleGattCharacteristic = BluetoothGattCharacteristic

typealias BleGattDescriptor = BluetoothGattDescriptor
