package com.spacer.sdk.services.cbLocker

import android.content.Context
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.models.myLocker.*
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.services.cbLocker.scan.CBLockerScanConnectService
import com.spacer.sdk.services.cbLocker.scan.CBLockerScanListService
import com.spacer.sdk.services.myLocker.MyLockerService
import java.util.concurrent.atomic.AtomicBoolean

class CBLockerService {
    fun scan(context: Context, token: String, callback: IResultCallback<List<SPRLockerModel>>) {
        if (isProcessing.compareAndSet(false, true)) {
            CBLockerScanListService().scan(context, token, createCallback(callback))
        }
    }

    fun put(context: Context, token: String, spacerId: String, callback: ICallback) {
        if (isProcessing.compareAndSet(false, true)) {
            CBLockerScanConnectService().put(context, token, spacerId, createCallback(callback))
        }
    }

    fun take(context: Context, token: String, spacerId: String, callback: ICallback) {
        if (isProcessing.compareAndSet(false, true)) {
            CBLockerScanConnectService().take(context, token, spacerId, createCallback(callback))
        }
    }

    fun openForMaintenance(context: Context, token: String, spacerId: String, callback: ICallback) {
        if (isProcessing.compareAndSet(false, true)) {
            CBLockerScanConnectService().openForMaintenance(
                context,
                token,
                spacerId,
                createCallback(callback)
            )
        }
    }

    fun takeWithUrlKey(context: Context, token: String, urlKey: String, callback: ICallback) {
        if (isProcessing.compareAndSet(false, true)) {
            MyLockerService().shareUrlKey(
                token,
                urlKey,
                object : IResultCallback<MyLockerModel> {
                    override fun onSuccess(result: MyLockerModel) {
                        isProcessing.set(false)
                        take(context, token, result.id, callback)
                    }

                    override fun onFailure(error: SPRError) {
                        isProcessing.set(false)
                        callback.onFailure(error)
                    }
                }
            )
        }
    }

    fun read(context: Context, spacerId: String, callback: IResultCallback<String>) {
        if (isProcessing.compareAndSet(false, true)) {
            CBLockerScanConnectService().read(context, spacerId, createCallback(callback))
        }
    }

    private fun <T> createCallback(callback: IResultCallback<T>): IResultCallback<T> {
        return object : IResultCallback<T> {
            override fun onSuccess(result: T) {
                isProcessing.set(false)
                callback.onSuccess(result)
            }

            override fun onFailure(error: SPRError) {
                isProcessing.set(false)
                callback.onFailure(error)
            }
        }
    }

    private fun createCallback(callback: ICallback): ICallback {
        return object : ICallback {
            override fun onSuccess() {
                isProcessing.set(false)
                callback.onSuccess()
            }

            override fun onFailure(error: SPRError) {
                isProcessing.set(false)
                callback.onFailure(error)
            }
        }
    }

    companion object {
        private val isProcessing = AtomicBoolean(false)
    }
}