@file:Suppress("unused", "unused")

package com.spacer.sdk.data.extensions

import android.util.Log

object LoggerExtensions {
    inline val <reified T> T.TAG: String get() = T::class.java.simpleName

    inline fun <reified T> T.logd(msg: String) = Log.d(TAG, msg)
    inline fun <reified T, V> T.logd(data: V) = Log.d(TAG, data.toString())
}