package com.spacer.example.presentation.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Build

class PermissionRequester(private val activity: Activity) {
    fun run() {
        val requestPermissions = permissionNames.keys.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (requestPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                requestPermissions,
                REQUEST_CODE
            )
        }
    }

    companion object {
        const val REQUEST_CODE = 1001
        private val permissionNamesSdkVersionOver30 = mapOf(
            Manifest.permission.BLUETOOTH_CONNECT to "Bluetooth接続",
            Manifest.permission.BLUETOOTH_SCAN to "Bluetooth検出",
            Manifest.permission.ACCESS_FINE_LOCATION to "Location"
        )

        private val permissionNamesSdkVersion30OrLess = mapOf(
            Manifest.permission.BLUETOOTH to "Bluetooth",
            Manifest.permission.ACCESS_FINE_LOCATION to "Location"
        )
        val permissionNames = if (Build.VERSION.SDK_INT > 30) permissionNamesSdkVersionOver30 else permissionNamesSdkVersion30OrLess
    }
}