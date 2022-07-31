package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter

fun SettingsPresenter.theme(){
    viewDataBinding.textAppTheme.setOnClickListener {
        showTheme()
    }
    when (Prefs.appTheme) {
        AppCompatDelegate.MODE_NIGHT_NO -> viewDataBinding.textAppTheme.setSubtitle(LocaleConstants.LIGHT_MODE.locale())
        AppCompatDelegate.MODE_NIGHT_YES -> viewDataBinding.textAppTheme.setSubtitle(LocaleConstants.DARK_MODE.locale())
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> viewDataBinding.textAppTheme.setSubtitle(LocaleConstants.SYSTEM.locale())
    }
}

fun SettingsPresenter.showTheme() {
    val prefs = Prefs
    val themes = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )
    val themesTitle = arrayOf(
        LocaleConstants.LIGHT_MODE.locale(),
        LocaleConstants.DARK_MODE.locale(),
        LocaleConstants.SYSTEM.locale()
    )
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.APP_THEME.locale())
        .setItems(themesTitle) { dialog, which ->
            prefs.appTheme = themes[which]
            AppCompatDelegate.setDefaultNightMode(Prefs.appTheme)
            val i: Intent? = mContext.packageManager
                .getLaunchIntentForPackage(mContext.packageName)
            i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mContext.startActivity(i)
        }.show()
}