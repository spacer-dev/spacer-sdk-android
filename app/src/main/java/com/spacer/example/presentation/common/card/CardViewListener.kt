package com.spacer.example.presentation.common.card

interface ICardSimpleViewListener {
    fun onClicked()
}

interface ICardInputViewListener {
    fun onClicked(text: String)
}
