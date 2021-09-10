package com.spacer.example.data

import com.spacer.example.BuildConfig
import com.spacer.sdk.SPR
import com.spacer.sdk.data.SPRConfig

object SdkConst {
    const val SdkToken = BuildConfig.SDK_TOKEN
    val SdkConfig = SPRConfig(baseURL = SPR.config.baseURL)
}