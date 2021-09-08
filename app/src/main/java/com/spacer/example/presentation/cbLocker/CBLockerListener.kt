package com.spacer.example.presentation.cbLocker

import androidx.fragment.app.Fragment
import com.spacer.example.data.SdkConst.SdkToken
import com.spacer.example.presentation.common.SdkRequester
import com.spacer.example.presentation.common.card.ICardInputViewListener
import com.spacer.example.presentation.common.card.ICardSimpleViewListener
import com.spacer.example.presentation.common.dialog.DialogMessage
import com.spacer.sdk.SPR
import com.spacer.sdk.models.sprLocker.SPRLockerModel

class CBLockerListener(private val fragment: Fragment) {
    private val service = SPR.cbLockerService()
    private val requester = SdkRequester(fragment)

    val scan = object : ICardSimpleViewListener {
        override fun onClicked() {
            val context = fragment.context ?: return

            requester.runList<SPRLockerModel>(DialogMessage.CbLockerScanSuccess) {
                service.scan(context, SdkToken, it)
            }
        }
    }

    val put = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            val context = fragment.context ?: return

            requester.run(DialogMessage.CbLockerPutSuccess) {
                service.put(context, SdkToken, text, it)
            }
        }
    }

    val take = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            val context = fragment.context ?: return

            requester.run(DialogMessage.CbLockerTakeSuccess) {
                service.take(context, SdkToken, text, it)
            }
        }
    }

    val takeUrlKey = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            val context = fragment.context ?: return

            requester.run(DialogMessage.CbLockerTakeUrlKeySuccess) {
                service.takeWithUrlKey(context, SdkToken, text, it)
            }
        }
    }
}
