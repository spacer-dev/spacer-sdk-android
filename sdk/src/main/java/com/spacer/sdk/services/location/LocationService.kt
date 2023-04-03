package com.spacer.sdk.services.location

import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.api.APIHeader
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.location.LocationGetReqData
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.location.LocationGetResDataMapper
import com.spacer.sdk.models.location.LocationModel

class LocationService {
    fun get(token: String, locationId: String, callback: IResultCallback<LocationModel>) {
        val params = LocationGetReqData(locationId)
        val mapper = LocationGetResDataMapper()

        api.location.getLocation(APIHeader.createHeader(token), params).enqueue(callback, mapper)
    }
}