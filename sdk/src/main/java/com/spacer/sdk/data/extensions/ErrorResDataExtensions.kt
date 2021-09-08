package com.spacer.sdk.data.extensions

import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.api.resData.ErrorResData

object ErrorResDataExtensions {
    fun ErrorResData.toSPRError(): SPRError = SPRError(code, message)
}