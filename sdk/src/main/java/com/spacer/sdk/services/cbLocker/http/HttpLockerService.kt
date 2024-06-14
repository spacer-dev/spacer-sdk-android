package com.spacer.sdk.services.cbLocker.http

import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.api.APIHeader
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.http.HttpLockerReqData

import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue

class HttpLockerService {

    fun put(token: String, spacerId: String, lat: Double, lng: Double, callback: ICallback) {
        val params = HttpLockerReqData(spacerId, lat, lng)

        api.httpLocker.put(APIHeader.createHeader(token), params).enqueue(callback)
    }

    fun take(token: String, spacerId: String, lat: Double, lng: Double, callback: ICallback) {
        val params = HttpLockerReqData(spacerId, lat, lng)

        api.httpLocker.take(APIHeader.createHeader(token), params).enqueue(callback)
    }

    fun openForMaintenance(token: String, spacerId: String, lat: Double, lng: Double, callback: ICallback) {
        val params = HttpLockerReqData(spacerId, lat, lng)

        api.httpLocker.openForMaintenance(APIHeader.createHeader(token), params).enqueue(callback)
    }
}