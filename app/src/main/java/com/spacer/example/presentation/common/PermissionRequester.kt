package com.spacer.example.presentation.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

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
        val permissionNames = mapOf(
            Manifest.permission.BLUETOOTH to "Bluetooth",
            Manifest.permission.ACCESS_FINE_LOCATION to "Location"
        )
    }
}