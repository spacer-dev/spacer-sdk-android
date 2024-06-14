package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerListReqData
import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerUnitGetReqData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerListResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerUnitGetResData
import retrofit2.Call
import retrofit2.http.*

interface ISPRLockerAPI {
    @POST("locker/spacer/list")
    fun getLockers(
        @HeaderMap headers: Map<String, String>, @Body params: SPRLockerListReqData
    ): Call<SPRLockerListResData>

    @GET("locker/spacer/{spacerId}")
    fun getLocker(
        @HeaderMap headers: Map<String, String>, @Path("spacerId") spacerId: String
    ): Call<SPRLockerGetResData>

    @POST("locker/unit/get")
    fun getUnits(
        @HeaderMap headers: Map<String, String>, @Body params: SPRLockerUnitGetReqData
    ): Call<SPRLockerUnitGetResData>
}
