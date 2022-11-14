package com.spacer.sdk.models.myLocker

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.myLocker.*

class MyMaintenanceLockerModel(
    val id: String,
    val lockedAt: String,
    val expiredAt: String?,
) {
    override fun toString() =
        "id:${id},lockedAt:${lockedAt},expiredAt:${expiredAt}"
}

fun MyMaintenanceLockerResData.toModel() = MyMaintenanceLockerModel(id, lockedAt, expiredAt)

class MyMaintenanceLockerListResDataMapper : IMapper<MyMaintenanceLockerGetResData, List<MyMaintenanceLockerModel>> {
    override fun map(source: MyMaintenanceLockerGetResData) = source.myMaintenanceLockers?.map { it.toModel() } ?: listOf()
}