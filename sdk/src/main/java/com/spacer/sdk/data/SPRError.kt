package com.spacer.sdk.data

import java.io.Serializable

data class SPRError(
    val code: String,
    val message: String
) : Serializable {

    override fun toString() = "$message (${code})"

    companion object {

        /**
         * API (E22002001 〜 E22002100)
         */
        val ApiFailed = SPRError("E22002001", "api request failed")
        val ApiBodyEmpty = SPRError("E22002002", "api request failed")

        /**
         * CB Scan  (E22010001 〜 E22011000)
         */
        val CBScanDisabled = SPRError("E22010001", "bluetooth is disabled")
        val CBScanDeviceNotFound = SPRError("E22010002", "bluetooth device is not found")

        /**
         * CB Gatt(E22011001 〜 E22012000)
         */
        val CBServiceNotFound = SPRError("E22011001", "peripheral service is not found")
        val CBCharacteristicNotFound = SPRError("E22011002", "peripheral characteristic is not found")
        val CBReadingCharacteristicFailed = SPRError("E22011003", "peripheral reading characteristic failed")
        val CBWritingCharacteristicFailed = SPRError("E22011004", "peripheral writing characteristic failed")
    }
}
