package com.spacer.sdk.data.extensions

object EnumExtensions {
    inline fun <reified T : Enum<T>> safeValueOf(type: String): T? {
        return try {
            return java.lang.Enum.valueOf(T::class.java, type)
        } catch (e: Exception) {
            null
        }
    }
}