package com.spacer.sdk.services.sprLocker

import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.api.APIHeader
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerListReqData
import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerUnitGetReqData
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.sprLocker.*

class SPRLockerService {
    fun getLockers(
        token: String, spacerIds: List<String>, callback: IResultCallback<List<SPRLockerModel>>
    ) {
        val params = SPRLockerListReqData(spacerIds)
        val mapper = SPRLockerListMapper()

        api.sprLocker.getLockers(APIHeader.createHeader(token), params).enqueue(callback, mapper)
    }

    fun getLocker(token: String, spacerId: String, callback: IResultCallback<SPRLockerModel>) {
        val mapper = SPRLockerGetMapper()

        api.sprLocker.getLocker(APIHeader.createHeader(token), spacerId).enqueue(callback, mapper)
    }

    fun getUnits(
        token: String, unitIds: List<String>, callback: IResultCallback<List<SPRLockerUnitModel>>
    ) {
        val params = SPRLockerUnitGetReqData(unitIds)
        val mapper = SPRLockerUnitGetReqDataMapper()

        api.sprLocker.getUnits(APIHeader.createHeader(token), params).enqueue(callback, mapper)
    }
}