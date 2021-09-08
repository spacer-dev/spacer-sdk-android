package com.spacer.sdk.services.cbLocker.scan

import android.content.Context
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.services.cbLocker.gatt.CBLockerGattPutService
import com.spacer.sdk.services.cbLocker.gatt.CBLockerGattTakeService

class CBLockerScanConnectService : CBLockerScanService() {
    private lateinit var spacerId: String
    private lateinit var callback: IResultCallback<CBLockerModel>

    private fun scan(context: Context, spacerId: String, callback: IResultCallback<CBLockerModel>) {
        this.spacerId = spacerId
        this.callback = callback

        val scanCallback = CBLockerScanConnectCallback()
        super.startScan(context, scanCallback)
    }

    fun put(context: Context, token: String, spacerId: String, callback: ICallback) {
        scan(
            context,
            spacerId,
            object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) = CBLockerGattPutService().connect(context, token, result, callback)
                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

    fun take(context: Context, token: String, spacerId: String, callback: ICallback) {
        scan(
            context,
            spacerId,
            object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) = CBLockerGattTakeService().connect(context, token, result, callback)
                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

    private inner class CBLockerScanConnectCallback : CBLockerScanCallback() {
        override fun onDiscovered(cbLocker: CBLockerModel) {
            if (cbLocker.spacerId == spacerId) {
                stopScan()
                callback.onSuccess(cbLocker)
            }
        }

        override fun onDelayed() = !isScanning

        override fun onFailure(error: SPRError) = callback.onFailure(error)
    }
}