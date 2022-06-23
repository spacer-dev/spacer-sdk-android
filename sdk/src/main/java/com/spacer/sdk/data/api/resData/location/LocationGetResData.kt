package com.spacer.sdk.data.api.resData.location

import com.spacer.sdk.data.api.resData.ErrorResData
import com.spacer.sdk.data.api.resData.IResData

data class LocationGetResData(
    val location: LocationResData?,
    override val result: Boolean,
    override val error: ErrorResData?,
) : IResData
