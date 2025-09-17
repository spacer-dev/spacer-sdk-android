package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.http.HttpLockerReqData
import com.spacer.sdk.data.api.reqData.key.*
import com.spacer.sdk.data.api.resData.http.HttpLockerResData
import com.spacer.sdk.data.api.resData.key.*
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.HeaderMap

interface IHttpLockerAPI {
    @POST("locationRPi/box/put")
    fun put(
        @HeaderMap headers: Map<String, String>, @Body params: HttpLockerReqData
    ): Call<HttpLockerResData>

    @POST("locationRPi/box/take")
    fun take(
        @HeaderMap headers: Map<String, String>, @Body params: HttpLockerReqData
    ): Call<HttpLockerResData>

    @POST("locationRPi/box/reservedOpen")
    fun reservedOpen(
        @HeaderMap headers: Map<String, String>, @Body params: HttpLockerReqData
    ): Call<HttpLockerResData>

    @POST("locationRPi/box/openForMaintenance")
    fun openForMaintenance(
        @HeaderMap headers: Map<String, String>, @Body params: HttpLockerReqData
    ): Call<HttpLockerResData>
}