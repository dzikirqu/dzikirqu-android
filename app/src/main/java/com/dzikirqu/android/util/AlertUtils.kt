package com.dzikirqu.android.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale


object AlertUtils {

    fun Context.showLocationPermissionAlert(
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this)
            .setTitle(LocaleConstants.LOCATION_PERMISSION.locale())
            .setMessage(LocaleConstants.THE_APP_NEEDS_LOCATION_PERMISSION_TO_GET_ACCURATE_PRAYER_TIME.locale())
            .setPositiveButton("Ok") { dialog, which ->
                onPositive?.invoke()
            }
            .setNegativeButton(LocaleConstants.CANCEL.locale()) { dialog, which ->
                onNegative?.invoke()
            }.show()
    }

    fun Context.showAutostartPermissionAlert(onPositive: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(LocaleConstants.AUTOSTART.locale())
            .setMessage(LocaleConstants.THE_APP_NEEDS_AUTOSTART_PERMISSION_TO_START_THE_ADZAN_AFTER_DEVICE_REBOOT.locale())
            .setPositiveButton("Ok") { dialog, which ->
                onPositive.invoke()
            }.show()
    }
}


