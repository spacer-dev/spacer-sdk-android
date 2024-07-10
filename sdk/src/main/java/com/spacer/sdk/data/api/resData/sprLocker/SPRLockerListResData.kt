package com.spacer.sdk.data.api.resData.sprLocker

import com.spacer.sdk.data.api.resData.ErrorResData
import com.spacer.sdk.data.api.resData.IResData

data class SPRLockerListResData(
    val spacers: List<SPRLockerResData>?,
    override val result: Boolean,
    override val error: ErrorResData?,
) : IResData

