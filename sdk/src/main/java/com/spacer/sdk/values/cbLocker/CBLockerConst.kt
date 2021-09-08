package com.spacer.sdk.values.cbLocker

import com.spacer.sdk.SPR
import java.util.*

class CBLockerConst {
    companion object {
        const val DEVICE_PUT_PREFIX = "543214723567xxxrw"
        const val DEVICE_TAKE_PREFIX = "543214723567xxxw"

        val ScanMills = SPR.config.scanMills
        val DeviceServiceUUID = UUID.fromString("0000ff10-0000-1000-8000-00805f9b34fb")!!
        val DeviceCharacteristicUUID = UUID.fromString("0000ff11-0000-1000-8000-00805f9b34fb")!!
    }
}