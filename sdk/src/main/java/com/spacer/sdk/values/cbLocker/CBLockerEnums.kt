package com.spacer.sdk.values.cbLocker

enum class CBLockerGattStatus {
    None,
    Write,
}

enum class CBLockerGattActionType {
    Put,
    Take,
    OpenForMaintenance
}