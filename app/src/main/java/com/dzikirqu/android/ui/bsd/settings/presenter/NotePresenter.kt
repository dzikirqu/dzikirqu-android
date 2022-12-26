package com.dzikirqu.android.ui.bsd.settings.presenter

import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter
import com.dzikirqu.android.util.RxBus

fun SettingsPresenter.note() {
    viewDataBinding.notesWysiwyg.isChecked = Prefs.noteWYSIWYGEnabled
    viewDataBinding.notesWysiwyg.setOnCheckedChangeListener { buttonView, isChecked ->
        Prefs.noteWYSIWYGEnabled = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }
}