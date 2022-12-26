package com.dzikirqu.android.ui.bsd.settings.presenter

import com.google.android.material.slider.Slider
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter
import com.dzikirqu.android.util.RxBus

fun SettingsPresenter.text(){
    val prefs = Prefs
    viewDataBinding.arabicSlider.value = prefs.arabicTextSize
    viewDataBinding.arabicSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
        prefs.arabicTextSize = value
        RxBus.getDefault().send(SettingsEvent())
    })

    viewDataBinding.translationSlider.value = prefs.translationTextSize
    viewDataBinding.translationSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
        prefs.translationTextSize = value
        RxBus.getDefault().send(SettingsEvent())
    })

    viewDataBinding.translationCheckbox.isChecked = prefs.isUseTranslation
    viewDataBinding.translationCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
        prefs.isUseTranslation = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }

    viewDataBinding.arabicCheckbox.isChecked = prefs.isUseArabic
    viewDataBinding.arabicCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
        prefs.isUseArabic = isChecked
        RxBus.getDefault().send(SettingsEvent())
    }

    viewDataBinding.notesFontSizeSlider.value = prefs.notesTextSize
    viewDataBinding.notesFontSizeSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
        prefs.notesTextSize = value
        RxBus.getDefault().send(SettingsEvent())
    })
}