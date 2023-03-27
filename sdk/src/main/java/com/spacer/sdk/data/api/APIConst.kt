package com.spacer.sdk.data.api

import com.spacer.sdk.SPR
import com.spacer.sdk.data.APIType

class APIConst {
    companion object {
        val BaseURL = SPR.config.baseURL
        val ApiType = APIType.init(SPR.config.apiType)
    }
}

class APIHeader {
    companion object {
        private const val ExTokenHeader = "X-Spacer-ExApp-Token"
        private const val AppTokenHeader = "Authorization"
        private const val appTypeHeader = "X-App-Type"

        fun createHeader(token: String): Map<String, String> {
            return if (APIConst.ApiType == APIType.App) {
                mapOf(
                    AppTokenHeader to "Bearer $token",
                    appTypeHeader to "SDK"
                )
            } else {
                mapOf(
                    ExTokenHeader to token
                )
            }
        }
    }
}