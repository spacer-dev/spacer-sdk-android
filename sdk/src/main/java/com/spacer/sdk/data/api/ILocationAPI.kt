package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.location.LocationGetReqData
import com.spacer.sdk.data.api.resData.location.LocationGetResData
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Body

interface ILocationAPI {
    @POST("location/get")
    fun getLocation(@Header(APIHeader.Token) token: String, @Body params: LocationGetReqData): Call<LocationGetResData>
}