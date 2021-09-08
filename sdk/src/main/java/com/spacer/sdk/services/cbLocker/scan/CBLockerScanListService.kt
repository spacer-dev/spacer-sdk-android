package com.spacer.sdk.services.cbLocker.scan

import android.content.Context
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.services.sprLocker.SPRLockerService

class CBLockerScanListService : CBLockerScanService() {
    private lateinit var token: String
    private lateinit var callback: IResultCallback<List<SPRLockerModel>>
    private var scanningList: MutableList<CBLockerModel> = mutableListOf()

    fun scan(context: Context, token: String, callback: IResultCallback<List<SPRLockerModel>>) {
        this.token = token
        this.callback = callback

        val scanCallback = CBLockerScanListCallback()
        super.startScan(context, scanCallback)
    }

    private inner class CBLockerScanListCallback : CBLockerScanCallback() {
        override fun onDiscovered(cbLocker: CBLockerModel) {
            val alreadyScanned = scanningList.any { it.spacerId == cbLocker.spacerId }
            if (!alreadyScanned) {
                scanningList.add(cbLocker)
            }
        }

        override fun onDelayed(): Boolean {
            return if (scanningList.size > 0) {
                scanningList.parse(callback)
                true
            } else {
                false
            }
        }

        override fun onFailure(error: SPRError) = callback.onFailure(error)

        private fun MutableList<CBLockerModel>.parse(callback: IResultCallback<List<SPRLockerModel>>) {
            val spacerIds = this.map { it.spacerId }
            SPRLockerService().getLockers(token, spacerIds, callback)
        }
    }
}
