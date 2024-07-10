package com.spacer.example.presentation.cbLocker

import androidx.lifecycle.ViewModel
import com.spacer.example.R
import com.spacer.example.presentation.common.card.CardViewModel
import com.spacer.example.presentation.common.header.HeaderViewModel

class CBLockerViewModel : ViewModel() {
    val header = HeaderViewModel().apply { init(R.string.cb_title) }
    val scan = CardViewModel().apply { init(R.string.cb_scan_lockers_title, R.string.cb_scan_lockers_desc) }
    val scanWithSpacerId = CardViewModel().apply { init(R.string.cb_scan_locker_title, R.string.cb_scan_locker_desc, R.string.cb_scan_locker_hint) }
    val put = CardViewModel().apply { init(R.string.cb_put_title, R.string.cb_put_desc, R.string.cb_put_hint) }
    val take = CardViewModel().apply { init(R.string.cb_take_title, R.string.cb_take_desc, R.string.cb_take_hint) }
    val openForMaintenance = CardViewModel().apply { init(R.string.cb_open_for_maintenance_title, R.string.cb_open_for_maintenance_desc, R.string.cb_open_for_maintenance_hint) }
    val takeUrlKey = CardViewModel().apply { init(R.string.cb_take_url_key_title, R.string.cb_take_url_key_desc, R.string.cb_take_url_key_hint) }
}