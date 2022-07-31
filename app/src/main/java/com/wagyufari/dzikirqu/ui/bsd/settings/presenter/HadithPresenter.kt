package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.app.AlertDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.constants.ReadModeConstants
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.TitleSubtitleView

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