package com.spacer.example.presentation.common.dialog

import android.annotation.SuppressLint
import com.spacer.example.Application
import com.spacer.example.R

@SuppressLint("StaticFieldLeak")
class DialogMessage {

    private val context = Application.context

    var title: String
    var body: String

    constructor(titleId: Int, bodyId: Int) {
        this.title = context.getString(titleId)
        this.body = context.getString(bodyId)
    }

    constructor(title: String, body: String) {
        this.title = title
        this.body = body
    }

    override fun toString() = "${title},${body}"

    companion object {
        val CbLockerScanLockersSuccess = DialogMessage(R.string.success_title, R.string.cb_scan_lockers_success_message)
        val CbLockerScanLockerSuccess = DialogMessage(R.string.success_title, R.string.cb_scan_locker_success_message)
        val CbLockerPutSuccess = DialogMessage(R.string.success_title, R.string.cb_put_success_message)
        val CbLockerTakeSuccess = DialogMessage(R.string.success_title, R.string.cb_take_success_message)
        val CbLockerReservedOpenSuccess = DialogMessage(R.string.success_title, R.string.cb_reserved_open_success_message)
        val CbLockerOpenForMaintenanceSuccess = DialogMessage(R.string.success_title, R.string.cb_open_for_maintenance_success_message)
        val CbLockerTakeUrlKeySuccess = DialogMessage(R.string.success_title, R.string.cb_take_url_key_success_message)
        val CbLockerReadSuccess = DialogMessage(R.string.success_title, R.string.cb_read_success_message)
        val CbLockerCheckDoorStatusSuccess = DialogMessage(R.string.success_title, R.string.cb_check_door_status_success_message)
        val MyLockerGetSuccess = DialogMessage(R.string.success_title, R.string.my_get_success_message)
        val MyLockerReserveSuccess = DialogMessage(R.string.success_title, R.string.my_reserve_success_message)
        val MyLockerReserveCancelSuccess = DialogMessage(R.string.success_title, R.string.my_reserve_cancel_success_message)
        val MyLockerShareUrlKeySuccess = DialogMessage(R.string.success_title, R.string.my_share_url_key_success_message)
        val MyMaintenanceLockerGetSuccess = DialogMessage(R.string.success_title, R.string.my_maintenance_success_message)
        val SPRLockerGetSuccess = DialogMessage(R.string.success_title, R.string.spr_locker_get_success_message)
        val SPRLockerGetUnitSuccess = DialogMessage(R.string.success_title, R.string.spr_unit_get_success_message)
        val SPRLocationGetSuccess = DialogMessage(R.string.success_title, R.string.spr_location_get_success_message)
    }
}
