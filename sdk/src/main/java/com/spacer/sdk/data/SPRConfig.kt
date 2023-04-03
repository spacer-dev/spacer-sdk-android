package com.spacer.sdk.data

class SPRConfig(
    val baseURL: String = SPRConst.BaseURL,
    val scanMills: Long = SPRConst.ScanMills,
    val maxRetryNum: Int = SPRConst.MaxRetryNum,
    val apiType: String = SPRConst.ApiType
) {
    companion object {
        val Default = SPRConfig()
    }
}