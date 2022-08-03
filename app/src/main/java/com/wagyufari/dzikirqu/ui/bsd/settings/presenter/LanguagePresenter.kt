package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.app.AlertDialog
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.ui.SplashActivity
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsActivity
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter
import com.wagyufari.dzikirqu.ui.main.MainActivity

fun SettingsPresenter.language(){
    viewDataBinding.textLanguage.setOnClickListener {
        showLanguage()
    }
}

fun SettingsPresenter.showLanguage() {
    val prefs = Prefs
    val languages = arrayOf("english", "bahasa")
    val languageTitle = arrayOf("English", "Indonesia")
    AlertDialog.Builder(mContext)
        .setTitle(LocaleConstants.LANGUAGE.locale())
        .setItems(languageTitle) { dialog, which ->
            prefs.language = languages[which]
            TaskStackBuilder.create(mContext)
                .addNextIntent(Intent(mContext, SplashActivity::class.java))
                .startActivities()
        }.show()
}
