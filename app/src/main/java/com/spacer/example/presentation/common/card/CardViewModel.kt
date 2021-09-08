package com.spacer.example.presentation.common.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {
    var titleId: Int? = null
    var descId: Int? = null
    var hintId: Int? = null
    var text: MutableLiveData<String> = MutableLiveData()

    fun init(titleId: Int?, descId: Int, hintId: Int? = null) {
        this.titleId = titleId
        this.descId = descId
        this.hintId = hintId
    }
}