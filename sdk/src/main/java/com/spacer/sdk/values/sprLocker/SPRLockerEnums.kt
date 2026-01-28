@file:Suppress("unused")

package com.spacer.sdk.values.sprLocker

import com.google.gson.annotations.SerializedName
import com.spacer.sdk.data.IEnumText

enum class SPRLockerStatus(override val text: String): IEnumText {
    Available("available"),
    Reserved("reserved"),
    @SerializedName("reservedOpen")
    ReservedOpen("reservedOpen"),
    Using("using"),
    Limited("limited"),
    Restricted("restricted"),
    Unknown("unknown"),
}