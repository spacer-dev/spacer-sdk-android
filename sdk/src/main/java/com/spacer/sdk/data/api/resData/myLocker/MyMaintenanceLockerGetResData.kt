package com.spacer.sdk.data.api.resData.myLocker

import com.spacer.sdk.data.api.resData.ErrorResData
import com.spacer.sdk.data.api.resData.IResData

data class MyMaintenanceLockerGetResData(
    val myMaintenanceLockers: List<MyMaintenanceLockerResData>?,
    override val result: Boolean,
    override val error: ErrorResData?,
) : IResData