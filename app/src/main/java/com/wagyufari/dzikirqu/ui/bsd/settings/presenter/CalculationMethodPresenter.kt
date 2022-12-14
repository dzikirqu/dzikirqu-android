package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.app.AlertDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter
import com.wagyufari.dzikirqu.util.praytimes.PrayTimeScript

fun SettingsPresenter.calculationMethod() {
    viewDataBinding.textCalculationMethod.setOnClickListener {
        showCalculationMethod()
    }
    viewDataBinding.textCalculationMethod.setSubtitle(calculationTitle[Prefs.calculationMethod - 1])
}

val calculationTitle = arrayOf(
    "University of Islamic Sciences, Karachi",
    "Islamic Society of North America (ISNA)",
    "Muslim World League (MWL)",
    "Umm al-Qura, Makkah",
    "Egyptian General Authority of Survey",
    "Kementrian Agama, Indonesia",
)

fun SettingsPresenter.showCalculationMethod() {
    val prefs = Prefs
    val praytime = PrayTimeScript()
    val calculation = arrayOf(
        praytime.Karachi,
        praytime.ISNA,
        praytime.MWL,
        praytime.Makkah,
        praytime.Egypt,
        praytime.Kemenag
    )
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.CALCULATION_METHOD.locale())
        .setItems(calculationTitle) { dialog, which ->
            prefs.calculationMethod = calculation[which]
            configureViews()
        }.show()
}