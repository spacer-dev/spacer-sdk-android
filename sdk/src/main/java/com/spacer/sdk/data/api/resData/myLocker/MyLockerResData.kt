package com.spacer.sdk.data.api.resData.myLocker

import java.io.Serializable

data class MyLockerResData(
    val id: String,
    val isReserved: Boolean,
    val reservedAt: String?,
    val expiredAt: String?,
    val lockedAt: String?,
    val urlKey: String,
) : Serializable