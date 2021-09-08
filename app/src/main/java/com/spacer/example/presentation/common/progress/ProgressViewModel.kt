package com.spacer.example.presentation.common.progress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isOverlay = MutableLiveData<Boolean>()

    init {
        isLoading.value = false
        isOverlay.value = false
    }

    fun enable(isOverlay: Boolean) {
        isLoading.value = true
        if (isOverlay) {
            this.isOverlay.value = true
        }
    }

    fun disable() {
        isLoading.value = false
        isOverlay.value = false
    }
}

enum class LoadingOption {
    Overlay,
    ScreenLock,
}