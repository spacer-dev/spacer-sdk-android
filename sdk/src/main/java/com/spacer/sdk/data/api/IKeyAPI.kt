package com.spacer.sdk.data.api

import com.spacer.sdk.data.api.reqData.key.KeyGenerateReqData
import com.spacer.sdk.data.api.reqData.key.KeyGenerateResultReqData
import com.spacer.sdk.data.api.reqData.key.KeyGetReqData
import com.spacer.sdk.data.api.reqData.key.KeyGetResultReqData
import com.spacer.sdk.data.api.resData.key.KeyGenerateResData
import com.spacer.sdk.data.api.resData.key.KeyGenerateResultResData
import com.spacer.sdk.data.api.resData.key.KeyGetResData
import com.spacer.sdk.data.api.resData.key.KeyGetResultResData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IKeyAPI {
    @POST("key/generate")
    fun generate(@Header(APIHeader.Token) token: String, @Body params: KeyGenerateReqData): Call<KeyGenerateResData>

    @POST("key/generateResult")
    fun generateResult(@Header(APIHeader.Token) token: String, @Body params: KeyGenerateResultReqData): Call<KeyGenerateResultResData>

    @POST("key/get")
    fun get(@Header(APIHeader.Token) token: String, @Body params: KeyGetReqData): Call<KeyGetResData>

    @POST("key/getResult")
    fun getResult(@Header(APIHeader.Token) token: String, @Body params: KeyGetResultReqData): Call<KeyGetResultResData>
}
