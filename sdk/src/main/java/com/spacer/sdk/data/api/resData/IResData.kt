package com.spacer.sdk.data.api.resData

import java.io.Serializable

interface IResData : Serializable {
    val result: Boolean
    val error: ErrorResData?
}