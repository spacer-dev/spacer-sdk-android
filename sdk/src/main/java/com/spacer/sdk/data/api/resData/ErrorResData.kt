package com.spacer.sdk.data.api.resData

import java.io.Serializable

data class ErrorResData(
    val code: String,
    val message: String,
) : Serializable