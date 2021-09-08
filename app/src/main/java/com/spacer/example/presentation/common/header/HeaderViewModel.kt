package com.spacer.example.presentation.common.header

import androidx.lifecycle.ViewModel

class HeaderViewModel : ViewModel() {
    var titleId: Int? = null

    fun init(titleId: Int) {
        this.titleId = titleId
    }
}