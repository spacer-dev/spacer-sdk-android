package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.myLocker.MyLockerReserveCancelReqData
import com.spacer.sdk.data.api.reqData.myLocker.MyLockerReserveReqData
import com.spacer.sdk.data.api.reqData.myLocker.MyLockerShareUrlKeyReqData
import com.spacer.sdk.data.api.resData.myLocker.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface IMyLockerAPI {
    @POST("myLocker/get")
    fun get(@HeaderMap headers: Map<String, String>): Call<MyLockerGetResData>

    @POST("myLocker/reserve")
    fun reserve(@HeaderMap headers: Map<String, String>, @Body params: MyLockerReserveReqData): Call<MyLockerReserveResData>

    @POST("myLocker/reserveCancel")
    fun reserveCancel(@HeaderMap headers: Map<String, String>, @Body params: MyLockerReserveCancelReqData): Call<MyLockerReserveCancelResData>

    @POST("myLocker/shared")
    fun shareUrlKey(@HeaderMap headers: Map<String, String>, @Body params: MyLockerShareUrlKeyReqData): Call<MyLockerShareUrlKeyResData>

    @POST("myLocker/maintenance/get")
    fun getMyMaintenance(@HeaderMap headers: Map<String, String>): Call<MyMaintenanceLockerGetResData>
}
