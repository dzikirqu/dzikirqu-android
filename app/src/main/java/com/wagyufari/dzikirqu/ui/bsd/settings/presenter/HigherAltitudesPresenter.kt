package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.app.AlertDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter
import com.wagyufari.dzikirqu.util.praytimes.PrayTimeScript

val higherTitle = arrayOf(
    LocaleConstants.NONE,
    LocaleConstants.MIDNIGHT,
    LocaleConstants.ONE_SEVENTH,
    LocaleConstants.ANGLE_BASED,
)

fun SettingsPresenter.higherAltitudes(){
    viewDataBinding.textHigherAltitudes.setOnClickListener {
        showHigherLatitudes()
    }
    viewDataBinding.textHigherAltitudes.setSubtitle(higherTitle[Prefs.higherLatitudes].locale())
}

fun SettingsPresenter.showHigherLatitudes() {
    val prefs = Prefs
    val praytime = PrayTimeScript()
    val higher = arrayOf(
        praytime.None,
        praytime.MidNight,
        praytime.OneSeventh,
        praytime.AngleBased,
    )
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.ADJUSTING_METHODS_FOR_HIGHER_LATITUDES.locale())
        .setItems(higherTitle) { dialog, which ->
            prefs.higherLatitudes = higher[which]
            configureViews()
        }.show()
}