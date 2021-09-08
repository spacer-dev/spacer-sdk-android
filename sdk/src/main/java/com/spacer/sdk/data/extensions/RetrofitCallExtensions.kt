@file:Suppress("unused", "unused")

package com.spacer.sdk.data.extensions

import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IMapper

import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.api.resData.IResData
import com.spacer.sdk.data.extensions.CallbackExtensions.toRetrofitCallback

import retrofit2.Call

object RetrofitCallExtensions {
    inline fun <reified TR : IResData> Call<TR>.enqueue(callback: ICallback) {
        enqueue(callback.toRetrofitCallback())
    }

    inline fun <reified TR : IResData, reified TD> Call<TR>.enqueue(callback: IResultCallback<TD>, mapper: IMapper<TR, TD>) {
        enqueue(callback.toRetrofitCallback(mapper))
    }
}