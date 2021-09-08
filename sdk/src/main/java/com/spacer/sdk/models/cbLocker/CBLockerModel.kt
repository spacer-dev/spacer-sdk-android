package com.spacer.sdk.models.cbLocker

import com.spacer.sdk.values.cbLocker.CBLockerGattStatus

class CBLockerModel(
    val spacerId: String,
    val address: String,
    var status: CBLockerGattStatus = CBLockerGattStatus.None,
) {
    override fun toString() =
        "spacerId:${spacerId},address:${address},status:${status}"

    fun update(status: CBLockerGattStatus) {
        this.status = status
    }

    fun reset() {
        this.status = CBLockerGattStatus.None
    }
}