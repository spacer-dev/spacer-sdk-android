package com.spacer.sdk.data.api.resData.sprLocker

import java.io.Serializable

data class SPRLockerResData(
    val id: String,
    val status: String,
    val size: String?,
    val closedWait: String,
    val version: String,
    val doorStatus: String,
    val doorStatusExpiredAt: String?
) : Serializable