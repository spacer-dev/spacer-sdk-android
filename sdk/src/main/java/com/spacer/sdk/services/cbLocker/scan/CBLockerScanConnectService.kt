package com.spacer.sdk.services.cbLocker.scan

import android.content.Context
import com.spacer.sdk.data.*
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.services.cbLocker.gatt.*
import com.spacer.sdk.services.cbLocker.http.HttpLockerService
import com.spacer.sdk.values.cbLocker.CBLockerConst
import com.spacer.sdk.values.cbLocker.CBLockerGattActionType

class CBLockerScanConnectService : CBLockerScanSingleService() {

    val notAvailableReadData: List<String> =
        listOf("openedExpired", "openedNotExpired", "closedExpired", "false")

    fun put(context: Context, token: String, spacerId: String, callback: ICallback) {
        getCBLockerModel(token, spacerId, object : IResultCallback<CBLockerModel> {
            override fun onSuccess(result: CBLockerModel) {
                if (result.isHttpSupported) {
                    // HTTP
                    execHttpLockerService(
                        token, spacerId, CBLockerGattActionType.Put, callback);
                } else {
                    // BLE
                    execBleLockerService(
                        context, token, spacerId, CBLockerGattActionType.Put, result, callback);
                }
            }
            override fun onFailure(error: SPRError) = callback.onFailure(error)
        })
    }

    fun take(context: Context, token: String, spacerId: String, callback: ICallback) {
        getCBLockerModel(token, spacerId, object : IResultCallback<CBLockerModel> {
            override fun onSuccess(result: CBLockerModel) {
                if (result.isHttpSupported) {
                    // HTTP
                    execHttpLockerService(
                        token, spacerId, CBLockerGattActionType.Take, callback);
                } else {
                    // BLE
                    execBleLockerService(
                        context, token, spacerId, CBLockerGattActionType.Take, result, callback);
                }
            }
            override fun onFailure(error: SPRError) = callback.onFailure(error)
        })
    }

    fun reservedOpen(context: Context, token: String, spacerId: String, callback: ICallback) {
        getCBLockerModel(token, spacerId, object : IResultCallback<CBLockerModel> {
            override fun onSuccess(result: CBLockerModel) {
                if (result.isHttpSupported) {
                    // HTTP
                    execHttpLockerService(
                        token, spacerId, CBLockerGattActionType.ReservedOpen, callback);
                } else {
                    // Not Supported
                    callback.onFailure(SPRError.CBServiceNotSupported)
                }
            }
            override fun onFailure(error: SPRError) = callback.onFailure(error)
        })
    }

    fun openForMaintenance(context: Context, token: String, spacerId: String, callback: ICallback) {
        getCBLockerModel(token, spacerId, object : IResultCallback<CBLockerModel> {
            override fun onSuccess(result: CBLockerModel) {
                if (result.isHttpSupported) {
                    // HTTP
                    execHttpLockerService(
                        token, spacerId, CBLockerGattActionType.OpenForMaintenance, callback);
                } else {
                    // BLE
                    execBleLockerService(
                        context, token, spacerId, CBLockerGattActionType.OpenForMaintenance, result, callback);
                }
            }
            override fun onFailure(error: SPRError) = callback.onFailure(error)
        })
    }

    fun checkDoorStatusAvailable(
        context: Context, token: String, spacerId: String, callback: IResultCallback<Boolean>
    ) {
        super.scan(
            context,
            spacerId,
            createCallBack(token, false, object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) {
                    if (!result.isScanned) {
                        callback.onSuccess(result.isHttpSupported)
                        return
                    }
                    val readConnectCallback = createReadCallBack(result, callback)
                    CBLockerGattReadService().connect(context, result, readConnectCallback)
                }

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            })
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
            override fun onFailure(error: SPRError) {
                retryOrFailure(
                    error, {
                        connectWithRetry(
                            type, context, token, cbLocker, callback, retryNum + 1, true
                        )}, retryNum + 1, cbLocker, callback)
                }
            }
            createCBLockerGattServiceWithConnect(type, context, token, cbLocker, retryCallback, isRetry)
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
                context, token, cbLocker, retryCallback, isRetry
            )
            CBLockerGattActionType.Take -> CBLockerGattTakeService().connect(
                context, token, cbLocker, retryCallback, isRetry
            )
            // Not Supported
            CBLockerGattActionType.ReservedOpen ->
                retryCallback.onFailure(SPRError.CBServiceNotSupported)
            CBLockerGattActionType.OpenForMaintenance -> CBLockerGattMaintenanceService().connect(
                context, token, cbLocker, retryCallback, isRetry
            )
        }
    }

    // BLE
    private fun execBleLockerService(
        context: Context,
        token: String,
        spacerId: String,
        type: CBLockerGattActionType,
        cbLocker: CBLockerModel,
        callback: ICallback
    ) {
        super.scan(
            context,
            spacerId,
            createCallBack(token, true, object : IResultCallback<CBLockerModel>{
                override fun onSuccess(result: CBLockerModel) {
                    result.isHttpSupported = cbLocker.isHttpSupported
                    result.doorStatus = cbLocker.doorStatus
                    connectWithRetry(
                        type, context, token, result, callback, 0, false)
                }

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            })
        )
    }

    // HTTP
    private fun execHttpLockerService(
        token: String,
        spacerId: String,
        type: CBLockerGattActionType,
        callback: ICallback
    ) {
        when (type) {
            CBLockerGattActionType.Put -> HttpLockerService().put(
                token, spacerId, callback
            )
            CBLockerGattActionType.Take -> HttpLockerService().take(
                token, spacerId, callback
            )
            CBLockerGattActionType.ReservedOpen -> HttpLockerService().reservedOpen(
                token, spacerId, callback
            )
            CBLockerGattActionType.OpenForMaintenance -> HttpLockerService().openForMaintenance(
                token, spacerId, callback
            )
        }
    }

    private fun createCallBack(
        token: String, isGetLocker: Boolean, callback: IResultCallback<CBLockerModel>
    ): IResultCallback<CBLockerModel> {
        return object : IResultCallback<CBLockerModel> {
            override fun onSuccess(cbLocker: CBLockerModel) {
                cbLocker.parse(token, true, isGetLocker, object : IResultCallback<SPRLockerModel> {
                    override fun onSuccess(result: SPRLockerModel) {
                        cbLocker.doorStatus = result.doorStatus
                        cbLocker.isHttpSupported = result.isHttpSupported
                        cbLocker.isScanned = true
                        callback.onSuccess(cbLocker)
                    }

                    override fun onFailure(error: SPRError) = callback.onFailure(error)
                })
            }

            override fun onFailure(error: SPRError) = callback.onFailure(error)
        }
    }

    private fun createReadCallBack(
        cbLocker: CBLockerModel, callback: IResultCallback<Boolean>
    ): IResultCallback<String> {
        return object : IResultCallback<String> {
            override fun onSuccess(result: String) {
                callback.onSuccess(
                    !notAvailableReadData.contains(result)
                )
            }

            override fun onFailure(error: SPRError) {
                callback.onSuccess(cbLocker.isHttpSupported)
            }
        }
    }

    //　ロッカー情報取得
    private fun getCBLockerModel(token: String, spacerId: String, callback: IResultCallback<CBLockerModel>)  {
        CBLockerModel(spacerId, "").parse(
            token,
            false,
            false,
            object : IResultCallback<SPRLockerModel> {
                override fun onSuccess(result: SPRLockerModel) {
                    val cbLocker = CBLockerModel(spacerId, "")
                    cbLocker.doorStatus = result.doorStatus;
                    cbLocker.isHttpSupported = result.isHttpSupported;
                    callback.onSuccess(cbLocker)
                }

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            }
        )
    }

}