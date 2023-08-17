package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.logger.LoggerReqData
import com.spacer.sdk.data.api.resData.IResData
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.HeaderMap

interface ILoggerAPI {
    @POST("logger/warn")
    fun sendWarn(@HeaderMap headers: Map<String, String>, @Body params: LoggerReqData): Call<IResData>
}