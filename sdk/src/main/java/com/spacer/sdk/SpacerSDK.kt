package com.spacer.sdk

import com.spacer.sdk.data.SPRConfig
import com.spacer.sdk.services.cbLocker.CBLockerService
import com.spacer.sdk.services.myLocker.MyLockerService
import com.spacer.sdk.services.sprLocker.SPRLockerService

class SpacerSDK {
    fun cbLockerService() = CBLockerService()
    fun myLockerService() = MyLockerService()
    fun sprLockerService() = SPRLockerService()

    var config = SPRConfig.Default
        private set

    fun configure(config: SPRConfig) {
        this.config = config
    }
}

val SPR = SpacerSDK()