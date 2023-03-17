package com.spacer.sdk.data

enum class APIType {
    Ex,
    App;

    companion object {
        fun init(value: String): APIType {
            return when (value) {
                "app" -> App
                else -> Ex
            }
        }
    }
}