package com.spacer.example.presentation.extensions

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import com.spacer.example.R
import com.spacer.example.presentation.common.dialog.DialogMessage
import com.spacer.example.presentation.common.progress.LoadingOption
import com.spacer.example.presentation.main.MainActivity
import com.spacer.sdk.data.SPRError

object FragmentExtensions {
    private val Fragment.mainActivity get() = activity as? MainActivity

    fun Fragment.startLoading(option: LoadingOption = LoadingOption.Overlay) = mainActivity?.startLoading(option)
    fun Fragment.stopLoading() = mainActivity?.stopLoading()

    fun Fragment.showSuccessDialog(message: DialogMessage) {
        AlertDialog.Builder(activity)
            .setTitle(message.title)
            .setMessage(message.body)
            .show()
    }

    fun Fragment.showErrorDialog(error: SPRError) {
        AlertDialog.Builder(activity)
            .setTitle(context?.getString(R.string.error_title))
            .setMessage(error.toString())
            .show()
    }
}