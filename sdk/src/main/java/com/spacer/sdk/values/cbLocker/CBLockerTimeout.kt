package com.spacer.sdk.values.cbLocker

import android.os.Handler
import android.os.Looper
import com.spacer.sdk.data.ICallback
import com.spacer.sdk.data.SPRError

class CBLockerTimeout(private val seconds: Long, private val timeoutAction: () -> Unit) {
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun set() {
        runnable = Runnable {
            timeoutAction.invoke()
        }
        handler.postDelayed(runnable!!, seconds)
    }

    fun clear() {
        runnable?.let { handler.removeCallbacks(it) }
    }
}

class CBLockerConnectTimeouts(retryOrFailure: (SPRError) -> Unit) {
    var start = CBLockerTimeout(CBLockerConst.StartTimeoutSeconds) { retryOrFailure(SPRError.CBConnectStartTimeout) }
    var discover = CBLockerTimeout(CBLockerConst.DiscoverTimeoutSeconds) { retryOrFailure(SPRError.CBConnectDiscoverTimeout) }
    var readBeforeWrite = CBLockerTimeout(CBLockerConst.ReadTimeoutSeconds) { retryOrFailure(SPRError.CBConnectReadTimeoutBeforeWrite) }
    var readAfterWrite = CBLockerTimeout(CBLockerConst.ReadTimeoutSeconds) { retryOrFailure(SPRError.CBConnectReadTimeoutAfterWrite) }
    var write = CBLockerTimeout(CBLockerConst.WriteTimeoutSeconds) { retryOrFailure(SPRError.CBConnectWriteTimeout) }
    var during = CBLockerTimeout(CBLockerConst.DuringTimeoutSeconds) { retryOrFailure(SPRError.CBConnectDuringTimeout) }

    fun clearAll() {
        arrayOf(start, discover, readBeforeWrite, readAfterWrite, write, during).forEach { timeout -> timeout.clear() }
    }
}