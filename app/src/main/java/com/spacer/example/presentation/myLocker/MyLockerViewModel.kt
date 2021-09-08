package com.spacer.example.presentation.myLocker

import androidx.lifecycle.ViewModel
import com.spacer.example.R
import com.spacer.example.presentation.common.card.CardViewModel
import com.spacer.example.presentation.common.header.HeaderViewModel

class MyLockerViewModel : ViewModel() {
    val header = HeaderViewModel().apply { init(R.string.my_title) }
    val get = CardViewModel().apply { init(R.string.my_get_title, R.string.my_get_desc) }
    var reserve = CardViewModel().apply { init(R.string.my_reserve_title, R.string.my_reserve_desc, R.string.my_reserve_hint) }
    var reserveCancel =
        CardViewModel().apply { init(R.string.my_reserve_cancel_title, R.string.my_reserve_cancel_desc, R.string.my_reserve_cancel_hint) }
    var shareUrlKey = CardViewModel().apply { init(R.string.my_share_url_key_title, R.string.my_share_url_key_desc, R.string.my_share_url_key_hint) }
}