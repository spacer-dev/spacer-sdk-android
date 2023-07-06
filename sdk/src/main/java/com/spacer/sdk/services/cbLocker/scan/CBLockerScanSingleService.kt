package com.spacer.sdk.services.cbLocker.scan

import android.content.Context
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerModel

open class CBLockerScanSingleService : CBLockerScanService() {
    private lateinit var spacerId: String
    private lateinit var callback: IResultCallback<CBLockerModel>

    protected fun scan(context: Context, spacerId: String, callback: IResultCallback<CBLockerModel>) {
        this.spacerId = spacerId
        this.callback = callback

        val scanCallback = CBLockerScanSingleCallback()
        super.startScan(context, scanCallback)
    }

    fun scanWithSpacerId(
        context: Context, token: String, spacerId: String, callback: IResultCallback<SPRLockerModel>
    ) {
        scan(context, spacerId, object : IResultCallback<CBLockerModel> {
            override fun onSuccess(result: CBLockerModel) {
                mutableListOf(result).parse(token, object : IResultCallback<List<SPRLockerModel>> {
                    override fun onSuccess(result: List<SPRLockerModel>) =
                        callback.onSuccess(result.first())

                    override fun onFailure(error: SPRError) = callback.onFailure(error)
                })
            }

            override fun onFailure(error: SPRError) = callback.onFailure(error)
        })
    }

    private inner class CBLockerScanSingleCallback : CBLockerScanCallback() {
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