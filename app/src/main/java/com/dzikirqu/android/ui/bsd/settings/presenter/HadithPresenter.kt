package com.dzikirqu.android.ui.bsd.settings.presenter

import android.app.AlertDialog
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.constants.ReadModeConstants
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.TitleSubtitleView

fun SettingsPresenter.hadith(){
    viewDataBinding.textHadithReadMode.setOnClickListener {
        val modes = arrayOf(
            LocaleConstants.VERTICAL_MODE.locale(),
            LocaleConstants.PAGED_MODE.locale()
        )
        AlertDialog.Builder(mContext)
            .setTitle(LocaleConstants.DEFAULT_READ_MODE.locale())
            .setItems(modes) { dialog, which ->
                when (which) {
                    0-> {
                        Prefs.defaultHadithReadMode = ReadModeConstants.VERTICAL
                    }
                    1 -> {
                        Prefs.defaultHadithReadMode = ReadModeConstants.PAGED
                    }
                }
                viewDataBinding.textHadithReadMode.updateReadModeText()
                RxBus.getDefault().send(SettingsEvent())
            }.show()
    }
    viewDataBinding.textHadithReadMode.updateReadModeText()
}


private fun TitleSubtitleView.updateReadModeText(){
    when (Prefs.defaultHadithReadMode) {
        ReadModeConstants.VERTICAL -> setSubtitle(LocaleConstants.VERTICAL_MODE.locale())
        ReadModeConstants.PAGED -> setSubtitle(LocaleConstants.PAGED_MODE.locale())
    }
}