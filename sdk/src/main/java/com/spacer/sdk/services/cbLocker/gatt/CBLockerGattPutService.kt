package com.spacer.sdk.services.cbLocker.gatt

import android.bluetooth.*
import android.content.Context
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.key.KeyGenerateReqData
import com.spacer.sdk.data.api.reqData.key.KeyGenerateResultReqData
import com.spacer.sdk.data.api.resData.key.KeyGenerateResData
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.cbLocker.CBLockerModel
import com.spacer.sdk.values.cbLocker.CBLockerConst

class CBLockerGattPutService : CBLockerGattService() {
    private lateinit var token: String
    private lateinit var callback: ICallback

    fun connect(context: Context, token: String, cbLocker: CBLockerModel, callback: ICallback) {
        this.token = token
        this.callback = callback

        val gattCallback = CBLockerGattPutCallback()
        super.connect(context, cbLocker, gattCallback, needsFirstRead = true)
    }

    private open inner class CBLockerGattPutCallback : CBLockerGattCallback() {
        override fun onKeyGet(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel, callback: IResultCallback<ByteArray>) {
            val params = KeyGenerateReqData(spacerId, characteristic.readData())
            val mapper = object : IMapper<KeyGenerateResData, ByteArray> {
                override fun map(source: KeyGenerateResData) = "${CBLockerConst.DEVICE_PUT_PREFIX}, ${source.key}".toByteArray()
            }
            api.key.generate(token, params).enqueue(callback, mapper)
        }

        override fun onFinished(characteristic: BluetoothGattCharacteristic, cbLocker: CBLockerModel, callback: ICallback) {
            val params = KeyGenerateResultReqData(spacerId, characteristic.readData())
            api.key.generateResult(token, params).enqueue(callback)
        }

        override fun onSuccess() = callback.onSuccess()
        override fun onFailure(error: SPRError) = callback.onFailure(error)
    }
}

