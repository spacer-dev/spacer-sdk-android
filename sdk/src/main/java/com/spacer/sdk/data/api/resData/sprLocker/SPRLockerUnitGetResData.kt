package com.spacer.sdk.data.api.resData.sprLocker

import com.spacer.sdk.data.api.resData.ErrorResData
import com.spacer.sdk.data.api.resData.IResData

data class SPRLockerUnitGetResData(
    val units: List<SPRLockerUnitResData>?,
    override val result: Boolean,
    override val error: ErrorResData?,
) : IResData

