package com.spacer.sdk.models.sprLocker

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerResData
import com.spacer.sdk.data.extensions.EnumExtensions.safeValueOf
import com.spacer.sdk.values.sprLocker.SPRLockerStatus

class SPRLockerModel(val id: String, val status: SPRLockerStatus, val size: String?, val closedWait: String, val version: String,  val doorStatus: String, val doorStatusExpiredAt: String?) {
    override fun toString() = "id:${id},status:${status.text},size:${size},closedWait:${closedWait},version:${version},doorStatus:${doorStatus},doorStatusExpiredAt:${doorStatusExpiredAt}"
}

fun SPRLockerResData.toModel(): SPRLockerModel {
    val status = safeValueOf(status, SPRLockerStatus.Unknown)
    return SPRLockerModel(id, status, size, closedWait, version, doorStatus, doorStatusExpiredAt)
}

class SPRLockerListMapper : IMapper<SPRLockerGetResData, List<SPRLockerModel>> {
    override fun map(source: SPRLockerGetResData) = source.spacers?.map { it.toModel() } ?: listOf()
}

