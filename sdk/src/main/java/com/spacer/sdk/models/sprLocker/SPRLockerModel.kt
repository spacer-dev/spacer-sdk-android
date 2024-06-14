package com.spacer.sdk.models.sprLocker

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.location.LocationGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerListResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerResData
import com.spacer.sdk.data.extensions.EnumExtensions.safeValueOf
import com.spacer.sdk.models.location.toModel
import com.spacer.sdk.values.sprLocker.SPRLockerStatus

class SPRLockerModel(
    val id: String,
    var address: String,
    val status: SPRLockerStatus,
    val size: String?,
    val closedWait: String,
    val version: String,
    val doorStatus: String,
    val doorStatusExpiredAt: String?,
    val isHttpSupported: Boolean,
    var isScanned: Boolean
) {
    override fun toString() =
        "id:${id},address:${address},status:${status.text},size:${size},closedWait:${closedWait},version:${version},doorStatus:${doorStatus},doorStatusExpiredAt:${doorStatusExpiredAt},isHttpSupported:${isHttpSupported}"
}

fun SPRLockerResData.toModel(): SPRLockerModel {
    val status = safeValueOf(status, SPRLockerStatus.Unknown)
    return SPRLockerModel(
        id,
        "",
        status,
        size,
        closedWait,
        version,
        doorStatus,
        doorStatusExpiredAt,
        isHttpSupported,
        isScanned = true
    )
}

class SPRLockerListMapper : IMapper<SPRLockerListResData, List<SPRLockerModel>> {
    override fun map(source: SPRLockerListResData) =
        source.spacers?.map { it.toModel() } ?: listOf()
}

class SPRLockerGetMapper : IMapper<SPRLockerGetResData, SPRLockerModel> {
    override fun map(source: SPRLockerGetResData) = source.spacer!!.toModel()
}