package com.spacer.sdk.services.myLocker

import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.myLocker.MyLockerReserveCancelReqData
import com.spacer.sdk.data.api.reqData.myLocker.MyLockerReserveReqData
import com.spacer.sdk.data.api.reqData.myLocker.MyLockerShareUrlKeyReqData
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.myLocker.*

class MyLockerService {
    fun get(token: String, callback: IResultCallback<List<MyLockerModel>>) {
        val mapper = MyLockerListResDataMapper()

        api.myLocker.get(token).enqueue(callback, mapper)
    }

    fun reserve(token: String, spacerId: String, callback: IResultCallback<MyLockerModel>) {
        val params = MyLockerReserveReqData(spacerId)
        val mapper = MyLockerReserveResDataMapper()

        api.myLocker.reserve(token, params).enqueue(callback, mapper)
    }

    fun reserveCancel(token: String, spacerId: String, callback: ICallback) {
        val params = MyLockerReserveCancelReqData(spacerId)

        api.myLocker.reserveCancel(token, params).enqueue(callback)
    }

    fun shareUrlKey(token: String, urlKey: String, callback: IResultCallback<MyLockerModel>) {
        val params = MyLockerShareUrlKeyReqData(urlKey)
        val mapper = MyLockerShareUrlKeyResDataMapper()

        api.myLocker.shareUrlKey(token, params).enqueue(callback, mapper)
    }
}