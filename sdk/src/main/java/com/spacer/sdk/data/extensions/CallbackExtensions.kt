@file:Suppress("unused", "unused")

package com.spacer.sdk.data.extensions

import com.spacer.sdk.data.*

import com.spacer.sdk.data.api.resData.IResData
import com.spacer.sdk.data.extensions.ErrorResDataExtensions.toSPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CallbackExtensions {
    inline fun <reified T : IResData> ICallback.toRetrofitCallback(): Callback<T> {
        val self = this
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, res: Response<T>) {
                res.tryFindError()?.let { return self.onFailure(it) }
                self.onSuccess()
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                logd(t.toString())
                return self.onFailure(SPRError.ApiFailed)
            }
        }
    }

    inline fun <reified T : IResData> IResultCallback<T>.toRetrofitCallback(): Callback<T> {
        val self = this
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, res: Response<T>) {
                res.tryFindError()?.let { return self.onFailure(it) }
                self.onSuccess(res.body()!!)
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                logd(t.toString())
                return self.onFailure(SPRError.ApiFailed)
            }
        }
    }

    inline fun <reified TR : IResData, reified TD> IResultCallback<TD>.toRetrofitCallback(mapper: IMapper<TR, TD>): Callback<TR> {
        val self = this
        return object : Callback<TR> {
            override fun onResponse(call: Call<TR>, res: Response<TR>) {
                res.tryFindError()?.let { return self.onFailure(it) }
                val data = mapper.map(res.body()!!)
                return self.onSuccess(data)
            }

            override fun onFailure(call: Call<TR?>, t: Throwable) {
                logd(t.toString())
                return self.onFailure(SPRError.ApiFailed)
            }
        }
    }

    inline fun <reified T : IResData> Response<T>.tryFindError(): SPRError? {
        if (isSuccessful) {
            val body = body() ?: return SPRError.ApiBodyEmpty
            return body.error?.toSPRError()
        } else {
            return SPRError.ApiFailed
        }
    }
}