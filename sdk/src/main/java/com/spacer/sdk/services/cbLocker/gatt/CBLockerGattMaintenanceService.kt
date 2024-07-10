package com.spacer.sdk.services.cbLocker.gatt

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.api.APIHeader
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.key.MaintenanceKeyGetReqData
import com.spacer.sdk.data.api.reqData.key.MaintenanceKeyGetResultReqData
import com.spacer.sdk.data.api.resData.key.MaintenanceKeyGetResData
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.values.cbLocker.CBLockerGattActionType

class CBLockerGattMaintenanceService : CBLockerGattService() {
    private lateinit var token: String
    private lateinit var callback: ICallback

    fun connect(context: Context, token: String, cbLocker: CBLockerModel, callback: ICallback, isRetry: Boolean) {
        this.token = token
        this.callback = callback

        val gattCallback = CBLockerGattMaintenanceCallback()
        super.connect(context, token, cbLocker, gattCallback, CBLockerGattActionType.OpenForMaintenance, isRetry)
    }

    private open inner class CBLockerGattMaintenanceCallback : CBLockerGattCallback() {
        override fun onKeyGet(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel, callback: IResultCallback<ByteArray>) {
            val params = MaintenanceKeyGetReqData(spacerId)
            val mapper = object : IMapper<MaintenanceKeyGetResData, ByteArray> {
                override fun map(source: MaintenanceKeyGetResData) = "${source.encryptedData}".toByteArray()
            }
            api.key.getMaintenance(APIHeader.createHeader(token), params).enqueue(callback, mapper)
        }

        override fun onFinished(callback: ICallback) {
            val params = MaintenanceKeyGetResultReqData(spacerId)
            api.key.getMaintenanceResult(APIHeader.createHeader(token), params).enqueue(callback)
        }

        override fun onSuccess() = callback.onSuccess()
        override fun onFailure(error: SPRError) = callback.onFailure(error)
    }
}