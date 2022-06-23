package com.spacer.sdk.data.api.resData.sprLocker

import java.io.Serializable

data class SPRLockerUnitResData(
    val id: String,
    val open: String?,
    val close: String?,
    val address: String?,
    val dispOrder: Number?,
    val lockerType: Number?,
    val spacers: List<SPRLockerResData>?
) : Serializable