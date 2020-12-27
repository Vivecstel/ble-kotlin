package com.steleot.sample.ui.utils

import android.bluetooth.BluetoothGattCharacteristic
import com.steleot.blekotlin.BleGattCharacteristic
import com.steleot.blekotlin.constants.BleGattCharacteristicUuids
import com.steleot.blekotlin.utils.toBluetoothUuidString

object BleCharacteristicUtils {

    fun BleGattCharacteristic.getDeviceName(): String? {
        if (BleGattCharacteristicUuids.DEVICE_NAME.toBluetoothUuidString()
                .equals(this.uuid.toString(), ignoreCase = true)
        ) {
            return getStringValue(0)
        }
        return null
    }

    fun BleGattCharacteristic.getHeartRate(): Int? {
        if (BleGattCharacteristicUuids.HEART_RATE_MEASUREMENT.toBluetoothUuidString()
                .equals(this.uuid.toString(), ignoreCase = true)
        ) {
            val flag: Int = this.properties
            val format: Int = if (flag and 0x01 != 0) {
                BluetoothGattCharacteristic.FORMAT_UINT16
            } else {
                BluetoothGattCharacteristic.FORMAT_UINT8
            }
            return this.getIntValue(format, 1)
        }
        return null
    }
}