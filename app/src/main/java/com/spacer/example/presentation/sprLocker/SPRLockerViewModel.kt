package com.spacer.example.presentation.sprLocker

import androidx.lifecycle.ViewModel
import com.spacer.example.R
import com.spacer.example.presentation.common.card.CardViewModel
import com.spacer.example.presentation.common.header.HeaderViewModel

class SPRLockerViewModel : ViewModel() {
    val header = HeaderViewModel().apply { init(R.string.spr_title) }
    val titleId = R.string.spr_title
    val locker = CardViewModel().apply { init(R.string.spr_locker_get_title, R.string.spr_locker_get_desc, R.string.spr_locker_get_hint) }
    var unit = CardViewModel().apply { init(R.string.spr_unit_get_title, R.string.spr_unit_get_desc, R.string.spr_unit_get_hint) }
}