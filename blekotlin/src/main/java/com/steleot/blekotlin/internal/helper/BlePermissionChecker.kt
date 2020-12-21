package com.steleot.blekotlin.internal.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import java.lang.ref.WeakReference

/**
 * The internal class that checks the needed permissions in order for the
 * [com.steleot.blekotlin.BleClient] to run properly.
 */
internal class BlePermissionChecker(
    context: Context
) {
    private val targetSdkVersion = context.applicationInfo.targetSdkVersion
    private val weakContext = WeakReference(context)

    /**
     * Checks if bluetooth permission is granted.
     * @return [Boolean] for the return value.
     */
    internal fun isBluetoothPermissionGranted() =
        isPermissionGranted(Manifest.permission.BLUETOOTH)

    /**
     * Checks if bluetooth admin permission is granted.
     * @return [Boolean] for the return value.
     */
    internal fun isBluetoothAdminPermissionGranted() =
        isPermissionGranted(Manifest.permission.BLUETOOTH_ADMIN)

    /**
     * Checks if location permission is granted. Check specific location permission per api lvl.
     * @return [Boolean] for the return value.
     */
    internal fun isLocationPermissionGranted(): Boolean {
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
        return weakContext.get()?.checkPermission(
            permission, Process.myPid(), Process.myUid()
        ) == PackageManager.PERMISSION_GRANTED
    }
}
