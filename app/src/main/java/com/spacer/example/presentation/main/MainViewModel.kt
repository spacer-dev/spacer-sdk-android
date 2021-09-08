package com.spacer.example.presentation.main

import androidx.lifecycle.ViewModel
import com.spacer.example.presentation.common.progress.ProgressViewModel

class MainViewModel : ViewModel() {
    var progress = ProgressViewModel()

    init {
        progress.isOverlay.value = false
    }
}
