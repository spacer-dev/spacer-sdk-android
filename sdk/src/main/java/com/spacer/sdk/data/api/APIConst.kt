package com.spacer.sdk.data.api

import com.spacer.sdk.SPR

class APIConst {
    companion object {
        val BaseURL = SPR.config.baseURL
    }
}

class APIHeader {
    companion object {
        const val Token = "X-Spacer-ExApp-Token"
    }
}