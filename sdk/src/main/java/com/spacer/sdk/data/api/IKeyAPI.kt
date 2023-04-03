package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.key.KeyGenerateReqData
import com.spacer.sdk.data.api.reqData.key.KeyGenerateResultReqData
import com.spacer.sdk.data.api.reqData.key.KeyGetReqData
import com.spacer.sdk.data.api.reqData.key.KeyGetResultReqData
import com.spacer.sdk.data.api.reqData.key.MaintenanceKeyGetReqData
import com.spacer.sdk.data.api.reqData.key.MaintenanceKeyGetResultReqData
import com.spacer.sdk.data.api.resData.key.KeyGenerateResData
import com.spacer.sdk.data.api.resData.key.KeyGenerateResultResData
import com.spacer.sdk.data.api.resData.key.KeyGetResData
import com.spacer.sdk.data.api.resData.key.KeyGetResultResData
import com.spacer.sdk.data.api.resData.key.MaintenanceKeyGetResData
import com.spacer.sdk.data.api.resData.key.MaintenanceKeyGetResultResData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface IKeyAPI {
    @POST("key/generate")
    fun generate(@HeaderMap headers: Map<String, String>, @Body params: KeyGenerateReqData): Call<KeyGenerateResData>

    @POST("key/generateResult")
    fun generateResult(@HeaderMap headers: Map<String, String>, @Body params: KeyGenerateResultReqData): Call<KeyGenerateResultResData>

    @POST("key/get")
    fun get(@HeaderMap headers: Map<String, String>, @Body params: KeyGetReqData): Call<KeyGetResData>

    @POST("key/getResult")
    fun getResult(@HeaderMap headers: Map<String, String>, @Body params: KeyGetResultReqData): Call<KeyGetResultResData>

    @POST("key/maintenance/get")
    fun getMaintenance(@HeaderMap headers: Map<String, String>, @Body params: MaintenanceKeyGetReqData): Call<MaintenanceKeyGetResData>

    @POST("key/maintenance/getResult")
    fun getMaintenanceResult(
        @HeaderMap headers: Map<String, String>,
        @Body params: MaintenanceKeyGetResultReqData
    ): Call<MaintenanceKeyGetResultResData>
}
