package com.spacer.example.presentation.common

import androidx.fragment.app.Fragment
import com.spacer.example.presentation.common.dialog.DialogMessage
import com.spacer.example.presentation.extensions.FragmentExtensions.showErrorDialog
import com.spacer.example.presentation.extensions.FragmentExtensions.showSuccessDialog
import com.spacer.example.presentation.extensions.FragmentExtensions.startLoading
import com.spacer.example.presentation.extensions.FragmentExtensions.stopLoading
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.IResultCallback
import com.spacer.sdk.data.SPRError
import com.spacer.sdk.data.extensions.LoggerExtensions.logd

open class SdkRequester(protected val fragment: Fragment) {
    fun run(message: DialogMessage, runnable: (callback: ICallback) -> Unit) {
        val callback = object : ICallback {
            override fun onSuccess() = success(message, "")
            override fun onFailure(error: SPRError) = fail(error)
        }

        fragment.startLoading()
        runnable.invoke(callback)
    }

    fun <T> runGet(message: DialogMessage, runnable: (callback: IResultCallback<T>) -> Unit) {
        val callback = object : IResultCallback<T> {
            override fun onSuccess(result: T) = success(message, result)
            override fun onFailure(error: SPRError) = fail(error)
        }

        fragment.startLoading()
        runnable.invoke(callback)
    }

    fun <T> runList(message: DialogMessage, runnable: (callback: IResultCallback<List<T>>) -> Unit) {
        val callback = object : IResultCallback<List<T>> {
            override fun onSuccess(result: List<T>) = success(message, result.joinToString("\n"))
            override fun onFailure(error: SPRError) = fail(error)
        }

        fragment.startLoading()
        runnable.invoke(callback)
    }

    protected fun <T> success(message: DialogMessage, result: T) {
        val newBody = "${message.body}\n${result}"
        val newMessage = DialogMessage(message.title, newBody)

        fragment.stopLoading()
        fragment.showSuccessDialog(newMessage)
    }

    protected fun fail(error: SPRError) {
        logd(error)
        fragment.stopLoading()
        fragment.showErrorDialog(error)
    }
}

