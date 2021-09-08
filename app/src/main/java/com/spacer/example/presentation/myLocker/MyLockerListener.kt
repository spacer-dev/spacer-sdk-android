package com.spacer.example.presentation.myLocker

import androidx.fragment.app.Fragment
import com.spacer.example.data.SdkConst.SdkToken
import com.spacer.example.presentation.common.SdkRequester
import com.spacer.example.presentation.common.card.ICardInputViewListener
import com.spacer.example.presentation.common.card.ICardSimpleViewListener
import com.spacer.example.presentation.common.dialog.DialogMessage
import com.spacer.sdk.SPR
import com.spacer.sdk.models.myLocker.MyLockerModel

class MyLockerListener(fragment: Fragment) {
    private val service = SPR.myLockerService()
    private val requester = SdkRequester(fragment)

    val get = object : ICardSimpleViewListener {
        override fun onClicked() {
            requester.runList<MyLockerModel>(DialogMessage.MyLockerGetSuccess) {
                service.get(SdkToken, it)
            }
        }
    }

    val reserve = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            requester.runGet<MyLockerModel>(DialogMessage.MyLockerReserveSuccess) {
                service.reserve(SdkToken, text, it)
            }
        }
    }

    val reserveCancel = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            requester.run(DialogMessage.MyLockerReserveCancelSuccess) {
                service.reserveCancel(SdkToken, text, it)
            }
        }
    }

    val shareUrlKey = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            requester.runGet<MyLockerModel>(DialogMessage.MyLockerShareUrlKeySuccess) {
                SPR.myLockerService().shareUrlKey(SdkToken, text, it)
            }
        }
    }
}
