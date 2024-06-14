package com.spacer.sdk.values.cbLocker

import com.spacer.sdk.SPR
import java.util.*

class CBLockerConst {
    companion object {
        const val DEVICE_PUT_PREFIX = "543214723567xxxrw"
        const val DEVICE_TAKE_PREFIX = "543214723567xxxw"
        const val StartTimeoutSeconds: Long = 5000
        const val DiscoverTimeoutSeconds: Long = 5000
        const val ReadTimeoutSeconds: Long = 5000
        const val WriteTimeoutSeconds: Long = 5000
        const val DuringTimeoutSeconds: Long = 60000
        const val AvailableDoorStatus = "closed"

        val ScanMills = SPR.config.scanMills
        val MaxRetryNum = SPR.config.maxRetryNum
        val DeviceServiceUUID = UUID.fromString("0000ff10-0000-1000-8000-00805f9b34fb")!!
        val DeviceCharacteristicUUID = UUID.fromString("0000ff11-0000-1000-8000-00805f9b34fb")!!
        val UsingReadData: List<String> = listOf("using")
        private val WriteReadData: List<String> = listOf("rwsuccess", "wsuccess")
        val UsingOrWriteReadData: List<String> = UsingReadData + WriteReadData
    }
}