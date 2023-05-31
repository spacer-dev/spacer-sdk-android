package com.spacer.sdk.services.cbLocker.scan

import android.content.Context
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IFailureCallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.services.cbLocker.gatt.*
import com.spacer.sdk.values.cbLocker.CBLockerConst
import com.spacer.sdk.values.cbLocker.CBLockerGattActionType

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
                override fun onSuccess(result: CBLockerModel) =
                    connectWithRetry(
                        CBLockerGattActionType.Put,
                        context,
                        token,
                        result,
                        callback,
                        0,
                        false
                    )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

    fun take(context: Context, token: String, spacerId: String, callback: ICallback) {
        scan(
            context,
            spacerId,
            object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) =
                    connectWithRetry(
                        CBLockerGattActionType.Take,
                        context,
                        token,
                        result,
                        callback,
                        0,
                        false
                    )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

    fun openForMaintenance(context: Context, token: String, spacerId: String, callback: ICallback) {
        scan(
            context,
            spacerId,
            object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) =
                    connectWithRetry(
                        CBLockerGattActionType.OpenForMaintenance,
                        context,
                        token,
                        result,
                        callback,
                        0,
                        false
                    )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

    fun read(context: Context, spacerId: String, callback: IResultCallback<String>) {
        scan(
            context,
            spacerId,
            object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) =
                    connectReadWithRetry(
                        context,
                        result,
                        callback,
                        0,
                        false
                    )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

    fun connectWithRetry(
        type: CBLockerGattActionType,
        context: Context,
        token: String,
        cbLocker: CBLockerModel,
        callback: ICallback,
        retryNum: Int,
        isRetry: Boolean
    ) {
        val retryCallback = object : ICallback {
            override fun onSuccess() = callback.onSuccess()
            override fun onFailure(error: SPRError) =
                retryOrFailure(
                    error,
                    {
                        connectWithRetry(
                            type,
                            context,
                            token,
                            cbLocker,
                            callback,
                            retryNum + 1,
                            true
                        )
                    },
                    retryNum + 1,
                    cbLocker,
                    callback
                )
        }
        createCBLockerGattServiceWithConnect(type, context, token, cbLocker, retryCallback, isRetry)
    }

    fun connectReadWithRetry(
        context: Context,
        cbLocker: CBLockerModel,
        callback: IResultCallback<String>,
        retryNum: Int,
        isRetry: Boolean
    ) {
        val retryCallback = object : IResultCallback<String> {
            override fun onSuccess(result: String) = callback.onSuccess(result)
            override fun onFailure(error: SPRError) =
                retryOrFailure(
                    error,
                    {
                        connectReadWithRetry(
                            context,
                            cbLocker,
                            callback,
                            retryNum + 1,
                            true
                        )
                    },
                    retryNum + 1,
                    cbLocker,
                    callback
                )
        }
        CBLockerGattReadService().connect(context, cbLocker, retryCallback, isRetry)
    }

    fun retryOrFailure(
        error: SPRError,
        executable: () -> Unit,
        retryNum: Int,
        cbLocker: CBLockerModel,
        callback: IFailureCallback
    ) {
        if (retryNum <= CBLockerConst.MaxRetryNum) {
            executable.invoke()
        } else {
            cbLocker.reset()
            callback.onFailure(error)
        }
    }

    private fun createCBLockerGattServiceWithConnect(
        type: CBLockerGattActionType,
        context: Context,
        token: String,
        cbLocker: CBLockerModel,
        retryCallback: ICallback,
        isRetry: Boolean
    ) {
        return when (type) {
            CBLockerGattActionType.Put -> CBLockerGattPutService().connect(
                context,
                token,
                cbLocker,
                retryCallback,
                isRetry
            )
            CBLockerGattActionType.Take -> CBLockerGattTakeService().connect(
                context,
                token,
                cbLocker,
                retryCallback,
                isRetry
            )
            CBLockerGattActionType.OpenForMaintenance -> CBLockerGattMaintenanceService().connect(
                context,
                token,
                cbLocker,
                retryCallback,
                isRetry
            )
        }
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