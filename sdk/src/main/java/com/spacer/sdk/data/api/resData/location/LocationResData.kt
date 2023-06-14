package com.spacer.sdk.data.api.resData.location

import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerUnitResData
import java.io.Serializable

data class LocationResData(
    val id: String,
    val name: String,
    val address: String,
    val detail: String,
    val open: String?,
    val close: String?,
    val doorWaitType: String,
    val units: List<SPRLockerUnitResData>?,
) : Serializable