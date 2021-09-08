package com.spacer.sdk.models.myLocker

import com.spacer.sdk.data.IMapper
import com.spacer.sdk.data.api.resData.myLocker.*

class MyLockerModel(
    val id: String,
    val isReserved: Boolean,
    val reservedAt: String?,
    val expiredAt: String?,
    val lockedAt: String?,
    val urlKey: String,
) {
    override fun toString() =
        "id:${id},isReserved:${isReserved},reservedAt:${reservedAt},expiredAt:${expiredAt},lockedAt:${lockedAt},urlKey:${urlKey}"
}

fun MyLockerResData.toModel() = MyLockerModel(id, isReserved, reservedAt, expiredAt, lockedAt, urlKey)

class MyLockerReserveResDataMapper : IMapper<MyLockerReserveResData, MyLockerModel> {
    override fun map(source: MyLockerReserveResData) = source.myLocker!!.toModel()
}

class MyLockerShareUrlKeyResDataMapper : IMapper<MyLockerShareUrlKeyResData, MyLockerModel> {
    override fun map(source: MyLockerShareUrlKeyResData) = source.myLocker!!.toModel()
}

class MyLockerListResDataMapper : IMapper<MyLockerGetResData, List<MyLockerModel>> {
    override fun map(source: MyLockerGetResData) = source.myLockers?.map { it.toModel() } ?: listOf()
}

