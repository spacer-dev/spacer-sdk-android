package com.spacer.sdk.data.api.resData.myLocker

import java.io.Serializable

data class MyMaintenanceLockerResData (
    val id: String,
    val lockedAt: String,
    val expiredAt: String?,
) : Serializable