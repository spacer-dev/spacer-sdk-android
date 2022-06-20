package com.spacer.sdk.models.sprLocker

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerUnitGetResData
import com.spacer.sdk.data.api.resData.sprLocker.SPRLockerUnitResData

class SPRLockerUnitModel(
    val id: String,
    val dispOrder: Number?,
    val lockerType: Number?,
    val spacers: List<SPRLockerModel>?,
) {
    override fun toString(): String {
        val spacersText = spacers?.joinToString("\n") { it.toString() }
        return "id:${id},spacers:\n${spacersText}"
    }
}

fun SPRLockerUnitResData.toModel(): SPRLockerUnitModel {
    val spacers = spacers?.map { it.toModel() }
    return SPRLockerUnitModel(id, dispOrder, lockerType, spacers)
}

class SPRLockerUnitGetReqDataMapper : IMapper<SPRLockerUnitGetResData, List<SPRLockerUnitModel>> {
    override fun map(source: SPRLockerUnitGetResData) = source.units?.map { it.toModel() } ?: listOf()
}

