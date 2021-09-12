package com.spacer.example.data

import com.spacer.example.BuildConfig
import com.spacer.sdk.data.SPRConfig

object SdkConst {
    const val SdkToken = BuildConfig.SDK_TOKEN
    private const val SdkBaseUrl = BuildConfig.SDK_BASE_URL

    val SdkConfig = SPRConfig(baseURL = SdkBaseUrl)
}