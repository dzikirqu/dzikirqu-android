package com.dzikirqu.android.ui.bsd.settings.presenter

import android.app.AlertDialog
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter
import com.dzikirqu.android.util.praytimes.PrayTimeScript


fun SettingsPresenter.asrJuristic(){
    viewDataBinding.textAsrJuristic.setOnClickListener {
        showAsrJuristic()
    }
    viewDataBinding.textAsrJuristic.setSubtitle(asrJuristicTitle[Prefs.asrJuristic])
}

val asrJuristicTitle = arrayOf(
    "Shafi'i, Maliki, Hanbali (Standard)",
    "Hanafi"
)

fun SettingsPresenter.showAsrJuristic() {
    val prefs = Prefs
    val praytime = PrayTimeScript()
    val asrJuristic = arrayOf(
        praytime.Shafii,
        praytime.Hanafi,
    )
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.ASR_JURISTIC.locale())
        .setItems(asrJuristicTitle) { dialog, which ->
            prefs.asrJuristic = asrJuristic[which]
            configureViews()
        }.show()
}