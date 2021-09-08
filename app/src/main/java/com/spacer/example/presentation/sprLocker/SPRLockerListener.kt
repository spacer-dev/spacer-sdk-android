package com.spacer.example.presentation.sprLocker

import androidx.fragment.app.Fragment
import com.spacer.example.data.SdkConst.SdkToken
import com.spacer.example.presentation.common.SdkRequester
import com.spacer.example.presentation.common.card.ICardInputViewListener
import com.spacer.example.presentation.common.dialog.DialogMessage
import com.spacer.sdk.SPR
import com.spacer.sdk.models.sprLocker.SPRLockerModel
import com.spacer.sdk.models.sprLocker.SPRLockerUnitModel


class SPRLockerListener(fragment: Fragment) {
    private val service = SPR.sprLockerService()
    private val requester = SdkRequester(fragment)

    val locker = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            val spacerIds = text.split(",").map { it.trim() }
            requester.runList<SPRLockerModel>(DialogMessage.SPRLockerGetSuccess) {
                service.getLockers(SdkToken, spacerIds, it)
            }
        }
    }

    val unit = object : ICardInputViewListener {
        override fun onClicked(text: String) {
            val unitIds = text.split(",").map { it.trim() }
            requester.runList<SPRLockerUnitModel>(DialogMessage.SPRLockerGetUnitSuccess) {
                service.getUnits(SdkToken, unitIds, it)
            }
        }
    }
}
