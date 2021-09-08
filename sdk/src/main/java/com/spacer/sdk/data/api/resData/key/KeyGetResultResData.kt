package com.spacer.sdk.data.api.resData.key

import com.spacer.sdk.data.api.resData.ErrorResData
import com.spacer.sdk.data.api.resData.IResData

data class KeyGetResultResData(
    override val result: Boolean,
    override val error: ErrorResData?,
) : IResData

