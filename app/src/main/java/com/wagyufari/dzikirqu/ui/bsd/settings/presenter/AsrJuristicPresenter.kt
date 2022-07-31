package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.app.AlertDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter
import com.wagyufari.dzikirqu.util.praytimes.PrayTimeScript


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