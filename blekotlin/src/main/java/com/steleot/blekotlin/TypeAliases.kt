package com.steleot.blekotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

typealias BleAdapter = BluetoothAdapter

typealias BleScanCallback = ScanCallback

typealias BleScanResult = ScanResult

typealias BleDevice = BluetoothDevice

typealias BleGatt = BluetoothGatt

typealias BleGattCallback = BluetoothGattCallback

typealias BleGattCharacteristic = BluetoothGattCharacteristic

typealias BleGattDescriptor = BluetoothGattDescriptor
