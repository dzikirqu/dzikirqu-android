package com.dzikirqu.android.ui.bsd.settings.presenter

import android.app.AlertDialog
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.ui.SplashActivity
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter

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
