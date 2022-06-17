package com.spacer.sdk.models.location

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.location.LocationGetResData
import com.spacer.sdk.data.api.resData.location.LocationResData
import com.spacer.sdk.models.sprLocker.SPRLockerUnitModel
import com.spacer.sdk.models.sprLocker.toModel

class LocationModel(
    val id: String,
    val name: String,
    val address: String,
    val detail: String,
    val open: String?,
    val close: String?,
    val units: List<SPRLockerUnitModel>?,
) {
    override fun toString(): String {
        val unitsText = units?.joinToString("\n") { it.toString() }
        return "id:${id},name:${name},address:${address},detail:${detail},open:${open},close:${close},units:\n${unitsText}"
    }
}

fun LocationResData.toModel(): LocationModel {
    val units = units?.map { it.toModel() }
    return LocationModel(id, name, address, detail, open, close, units)
}

class LocationGetResDataMapper : IMapper<LocationGetResData, LocationModel> {
    override fun map(source: LocationGetResData) = source.location!!.toModel()
}
