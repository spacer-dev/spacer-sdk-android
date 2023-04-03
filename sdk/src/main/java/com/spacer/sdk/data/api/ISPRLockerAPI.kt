package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerGetReqData
import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerUnitGetReqData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerUnitGetResData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ISPRLockerAPI {
    @POST("locker/spacer/get")
    fun getLockers(@HeaderMap headers: Map<String, String>, @Body params: SPRLockerGetReqData): Call<SPRLockerGetResData>

    @POST("locker/unit/get")
    fun getUnits(@HeaderMap headers: Map<String, String>, @Body params: SPRLockerUnitGetReqData): Call<SPRLockerUnitGetResData>
}
