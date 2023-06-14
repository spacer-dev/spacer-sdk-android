package com.spacer.example.presentation.cbLocker

import androidx.lifecycle.ViewModel
import com.spacer.example.R
import com.spacer.example.presentation.common.card.CardViewModel
import com.spacer.example.presentation.common.header.HeaderViewModel

class CBLockerViewModel : ViewModel() {
    val header = HeaderViewModel().apply { init(R.string.cb_title) }
    val scan = CardViewModel().apply { init(R.string.cb_scan_title, R.string.cb_scan_desc) }
    val put = CardViewModel().apply { init(R.string.cb_put_title, R.string.cb_put_desc, R.string.cb_put_hint) }
    val take = CardViewModel().apply { init(R.string.cb_take_title, R.string.cb_take_desc, R.string.cb_take_hint) }
    val openForMaintenance = CardViewModel().apply { init(R.string.cb_open_for_maintenance_title, R.string.cb_open_for_maintenance_desc, R.string.cb_open_for_maintenance_hint) }
    val takeUrlKey = CardViewModel().apply { init(R.string.cb_take_url_key_title, R.string.cb_take_url_key_desc, R.string.cb_take_url_key_hint) }
    val read = CardViewModel().apply { init(R.string.cb_read_title, R.string.cb_read_desc, R.string.cb_read_hint) }
}