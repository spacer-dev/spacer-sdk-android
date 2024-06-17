package com.spacer.sdk.models.cbLocker

import com.spacer.sdk.values.cbLocker.CBLockerGattStatus

class CBLockerModel(
    val spacerId: String,
    val address: String,
    var status: CBLockerGattStatus = CBLockerGattStatus.None,
    var doorStatus: String = "",
    var isHttpSupported: Boolean = false,
    var isScanned: Boolean = false,
    var hasBLERetried: Boolean = false
) {
    override fun toString() =
        "spacerId:${spacerId},address:${address},status:${status},doorStatus:${doorStatus},isHttpSupported:${isHttpSupported},isScanned:${isScanned},hasBLERetried:${hasBLERetried}"

    fun update(status: CBLockerGattStatus) {
        this.status = status
    }

    fun reset() {
        this.status = CBLockerGattStatus.None
    }
}