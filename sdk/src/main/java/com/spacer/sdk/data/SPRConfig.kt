package com.spacer.sdk.data

class SPRConfig(val baseURL: String = SPRConst.BaseURL, val scanMills: Long = SPRConst.ScanMills) {
    companion object {
        val Default = SPRConfig()
    }
}