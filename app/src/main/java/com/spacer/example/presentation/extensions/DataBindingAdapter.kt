package com.spacer.example.presentation.extensions

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("resourceText")
    fun TextView.setResourceText(resId: Int?) {
        resId ?: return
        this.text = context.getString(resId)
    }

    @JvmStatic
    @BindingAdapter("resourceHint")
    fun TextView.setResourceHint(resId: Int?) {
        resId ?: return
        this.hint = context.getString(resId)
    }

    @JvmStatic
    @BindingAdapter("enabledByValue")
    fun Button.setEnabledByValue(value: String?) {
        this.isEnabled = (value != null) then true ?: false
    }

    @JvmStatic
    @BindingAdapter("visibleOrGone")
    fun View.setVisibleOrGone(value: Boolean?) {
        this.visibility = (value == true) then View.VISIBLE ?: View.GONE
    }

    private infix fun <T> Boolean.then(other: T) = if (this) other else null
}