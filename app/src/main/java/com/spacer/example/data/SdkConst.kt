package com.spacer.example.data

import com.spacer.example.BuildConfig
import com.spacer.sdk.SPR
import com.spacer.sdk.data.SPRConfig

object SdkConst {
    const val SdkToken = BuildConfig.SDK_TOKEN
    val SdkConfig = SPRConfig(baseURL = "http://120.143.1.101:8008/exApp")
    //        val config = SPRConfig(baseURL = "https://api-vsv0ukl18tz6dm.spacer.co.jp")

//    val SdkConfig = SPRConfig(baseURL = SPR.config.baseURL)
}