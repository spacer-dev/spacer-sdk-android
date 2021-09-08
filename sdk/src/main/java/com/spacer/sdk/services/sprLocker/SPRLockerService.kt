package com.spacer.sdk.services.sprLocker

import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.api.api
import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerGetReqData
import com.spacer.sdk.data.api.reqData.sprLocker.SPRLockerUnitGetReqData
import com.spacer.sdk.data.extensions.RetrofitCallExtensions.enqueue
import com.spacer.sdk.models.sprLocker.SPRLockerListMapper
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerUnitGetReqDataMapper
import com.spacer.sdk.models.sprLocker.SPRLockerUnitModel

class SPRLockerService {
    fun getLockers(token: String, spacerIds: List<String>, callback: IResultCallback<List<SPRLockerModel>>) {
        val params = SPRLockerGetReqData(spacerIds)
        val mapper = SPRLockerListMapper()

        api.sprLocker.getLockers(token, params).enqueue(callback, mapper)
    }

    fun getUnits(token: String, unitIds: List<String>, callback: IResultCallback<List<SPRLockerUnitModel>>) {
        val params = SPRLockerUnitGetReqData(unitIds)
        val mapper = SPRLockerUnitGetReqDataMapper()

        api.sprLocker.getUnits(token, params).enqueue(callback, mapper)
    }
}