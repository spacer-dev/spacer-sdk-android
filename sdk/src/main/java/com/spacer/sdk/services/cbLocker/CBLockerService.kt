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

class CBLockerService {
    fun scan(context: Context, token: String, callback: IResultCallback<List<SPRLockerModel>>) {
        CBLockerScanListService().scan(context, token, callback)
    }

    fun put(context: Context, token: String, spacerId: String, callback: ICallback) {
        CBLockerScanConnectService().put(context, token, spacerId, callback)
    }

    fun take(context: Context, token: String, spacerId: String, callback: ICallback) {
        CBLockerScanConnectService().take(context, token, spacerId, callback)
    }

    fun takeWithUrlKey(context: Context, token: String, urlKey: String, callback: ICallback) {
        MyLockerService().shareUrlKey(
            token,
            urlKey,
            object : IResultCallback<MyLockerModel> {
                override fun onSuccess(result: MyLockerModel) = take(context, token, result.id, callback)
                override fun onFailure(error: SPRError) = callback.onFailure(error)
            })

    }
}