package com.spacer.sdk.data

interface ICallback : IFailureCallback {
    fun onSuccess() {}
}

interface IResultCallback<T> : IFailureCallback {
    fun onSuccess(result: T)
}

interface IFailureCallback {
    fun onFailure(error: SPRError) {}
}