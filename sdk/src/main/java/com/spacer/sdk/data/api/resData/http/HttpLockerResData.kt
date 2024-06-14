package com.spacer.sdk.data.api.resData.http

import com.spacer.sdk.data.api.resData.ErrorResData
import com.spacer.sdk.data.api.resData.IResData

data class HttpLockerResData(
    override val result: Boolean,
    override val error: ErrorResData?,
) : IResData

