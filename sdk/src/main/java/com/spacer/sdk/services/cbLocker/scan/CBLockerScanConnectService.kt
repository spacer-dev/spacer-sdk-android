package com.spacer.sdk.services.cbLocker.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.spacer.sdk.data.*
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.services.cbLocker.gatt.*
import com.spacer.sdk.services.cbLocker.http.HttpLockerService
import com.spacer.sdk.values.cbLocker.CBLockerConst
import com.spacer.sdk.values.cbLocker.CBLockerGattActionType
import com.spacer.sdk.values.cbLocker.CBLockerGattStatus

class CBLockerScanConnectService : CBLockerScanSingleService() {

    val httpFallbackErrors: List<SPRError> = listOf(
        SPRError.CBServiceNotFound,
        SPRError.CBCharacteristicNotFound,
        SPRError.CBReadingCharacteristicFailed,
        SPRError.CBConnectStartTimeout,
        SPRError.CBConnectDiscoverTimeout,
        SPRError.CBConnectReadTimeoutBeforeWrite
    )
    val notAvailableReadData: List<String> =
        listOf("openedExpired", "openedNotExpired", "closedExpired", "false")

    fun put(context: Context, token: String, spacerId: String, callback: ICallback) {
        super.scan(
            context,
            spacerId,
            createCallBack(context, token, spacerId, object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) = connectWithRetry(
                    CBLockerGattActionType.Put, context, token, result, callback, 0, false
                )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            })
        )
    }

    fun take(context: Context, token: String, spacerId: String, callback: ICallback) {
        super.scan(
            context,
            spacerId,
            createCallBack(context, token, spacerId, object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) = connectWithRetry(
                    CBLockerGattActionType.Take, context, token, result, callback, 0, false
                )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            })
        )
    }

    fun openForMaintenance(context: Context, token: String, spacerId: String, callback: ICallback) {
        super.scan(
            context,
            spacerId,
            createCallBack(context, token, spacerId, object : IResultCallback<CBLockerModel> {
                override fun onSuccess(result: CBLockerModel) = connectWithRetry(
                    CBLockerGattActionType.OpenForMaintenance,
                    context,
                    token,
                    result,
                    callback,
                    0,
                    false
                )

                override fun onFailure(error: SPRError) = callback.onFailure(error)
            })
        )
    }

    fun checkDoorStatusAvailable(
        context: Context, token: String, spacerId: String, callback: IResultCallback<Boolean>
    ) {
        super.scan(
            context,
            spacerId,
            createCallBack(context, token, spacerId, object : IResultCallback<CBLockerModel> {
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
        if (cbLocker.isHttpSupported && !cbLocker.isScanned && PermissionChecker.isLocationPermitted(context)) {
            execHttpLockerService(context, type, token, cbLocker.spacerId, callback)
        } else {
            val retryCallback = object : ICallback {
                override fun onSuccess() = callback.onSuccess()
                override fun onFailure(error: SPRError) {
                    if (cbLocker.isHttpSupported && !cbLocker.hasBLERetried && httpFallbackErrors.contains(
                            error
                        ) && PermissionChecker.isLocationPermitted(context)
                    ) {
                        execHttpLockerService(context, type, token, cbLocker.spacerId, callback)
                    } else {
                        retryOrFailure(
                            error, {
                                connectWithRetry(
                                    type, context, token, cbLocker, callback, retryNum + 1, true
                                )
                            }, retryNum + 1, cbLocker, callback
                        )
                    }
                }
            }
            createCBLockerGattServiceWithConnect(type, context, token, cbLocker, retryCallback, isRetry)
        }
    }

    fun retryOrFailure(
        error: SPRError,
        executable: () -> Unit,
        retryNum: Int,
        cbLocker: CBLockerModel,
        callback: IFailureCallback
    ) {
        if (retryNum <= CBLockerConst.MaxRetryNum) {
            cbLocker.hasBLERetried = true
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
            CBLockerGattActionType.OpenForMaintenance -> CBLockerGattMaintenanceService().connect(
                context, token, cbLocker, retryCallback, isRetry
            )
        }
    }

    private fun execHttpLockerService(
        context: Context,
        type: CBLockerGattActionType,
        token: String,
        spacerId: String,
        callback: ICallback
    ) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        @SuppressLint("MissingPermission") val myLocation =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val lat = myLocation?.latitude
        val lng = myLocation?.longitude
        when (type) {
            CBLockerGattActionType.Put -> HttpLockerService().put(
                token, spacerId, lat, lng, callback
            )
            CBLockerGattActionType.Take -> HttpLockerService().take(
                token, spacerId, lat, lng, callback
            )
            CBLockerGattActionType.OpenForMaintenance -> HttpLockerService().openForMaintenance(
                token, spacerId, lat, lng, callback
            )
        }
    }

    private fun createCallBack(
        context: Context, token: String, spacerId: String, callback: IResultCallback<CBLockerModel>
    ): IResultCallback<CBLockerModel> {
        return object : IResultCallback<CBLockerModel> {
            override fun onSuccess(cbLocker: CBLockerModel) {
                cbLocker.parse(token, true, object : IResultCallback<SPRLockerModel> {
                    override fun onSuccess(result: SPRLockerModel) {
                        cbLocker.doorStatus = result.doorStatus
                        cbLocker.isHttpSupported = result.isHttpSupported
                        cbLocker.isScanned = true
                        callback.onSuccess(cbLocker)
                    }

                    override fun onFailure(error: SPRError) = callback.onFailure(error)
                })
            }

            override fun onFailure(error: SPRError) {
                val cbLocker = CBLockerModel(spacerId, "")
                cbLocker.parse(token, false, object : IResultCallback<SPRLockerModel> {
                    override fun onSuccess(result: SPRLockerModel) {
                        if (result.isHttpSupported && PermissionChecker.isLocationPermitted(context)) {
                            cbLocker.doorStatus = result.doorStatus
                            cbLocker.isHttpSupported = true
                            cbLocker.isScanned = false
                            callback.onSuccess(cbLocker)
                        } else {
                            callback.onFailure(error)
                        }
                    }

                    override fun onFailure(error: SPRError) = callback.onFailure(error)
                })
            }
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
}