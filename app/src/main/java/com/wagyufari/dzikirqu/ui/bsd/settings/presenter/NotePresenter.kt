package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter
import com.wagyufari.dzikirqu.util.RxBus

fun SettingsPresenter.note() {
    viewDataBinding.notesWysiwyg.isChecked = Prefs.noteWYSIWYGEnabled
    viewDataBinding.notesWysiwyg.setOnCheckedChangeListener { buttonView, isChecked ->
        Prefs.noteWYSIWYGEnabled = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }
}