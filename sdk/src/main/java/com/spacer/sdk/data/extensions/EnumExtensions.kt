package com.spacer.sdk.data.extensions

import com.spacer.sdk.data.IEnumText

object EnumExtensions {
    inline fun <reified T> safeValueOf(text: String, default: T): T
            where T : Enum<T>, T : IEnumText {
        return try {
            return enumValues<T>().firstOrNull { it.text == text } ?: default
        } catch (e: Exception) {
            default
        }
    }
}