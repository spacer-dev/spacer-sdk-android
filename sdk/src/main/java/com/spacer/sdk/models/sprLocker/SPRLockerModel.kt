package com.spacer.sdk.models.sprLocker

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerResData
import com.spacer.sdk.data.extensions.EnumExtensions.safeValueOf
import com.spacer.sdk.values.sprLocker.SPRLockerStatus

class SPRLockerModel(val id: String, val status: SPRLockerStatus, val size: String?) {
    override fun toString() = "id:${id},status:${status},size:${size}"
}

fun SPRLockerResData.toModel(): SPRLockerModel {
    val statusEnum = safeValueOf(status) ?: SPRLockerStatus.unknown
    return SPRLockerModel(id, statusEnum, size)
}

class SPRLockerListMapper : IMapper<SPRLockerGetResData, List<SPRLockerModel>> {
    override fun map(source: SPRLockerGetResData) = source.spacers?.map { it.toModel() } ?: listOf()
}

