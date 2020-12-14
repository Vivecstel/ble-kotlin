package com.steleot.blekotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process

class PermissionChecker(
    private val context: Context
) {

    private val targetSdkVersion = context.applicationInfo.targetSdkVersion

    fun isBluetoothPermissionGranted() =
        isPermissionGranted(Manifest.permission.BLUETOOTH)

    fun isBluetoothAdminPermissionGranted() =
        isPermissionGranted(Manifest.permission.BLUETOOTH_ADMIN)

    fun isLocationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else {
            if (targetSdkVersion > Build.VERSION_CODES.P) {
                isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun isPermissionGranted(
        permission: String
    ): Boolean {
        return context.checkPermission(
            permission, Process.myPid(), Process.myUid()
        ) == PackageManager.PERMISSION_GRANTED
    }
}