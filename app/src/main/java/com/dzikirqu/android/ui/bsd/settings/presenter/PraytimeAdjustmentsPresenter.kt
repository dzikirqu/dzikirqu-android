package com.dzikirqu.android.ui.bsd.settings.presenter

import android.app.Dialog
import android.view.LayoutInflater
import com.google.android.material.slider.Slider
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.DialogPraytimeAdjustmentsBinding
import com.dzikirqu.android.ui.bsd.settings.SettingsPresenter

fun SettingsPresenter.praytimeAdjustments(){

    viewDataBinding.textPraytimeAdjustments.setOnClickListener {
        showPraytimeAdjustments()
    }
    viewDataBinding.textPraytimeAdjustments.setSubtitle("${Prefs.fajrOffset},${Prefs.dhuhrOffset},${Prefs.asrOffset},${Prefs.maghribOffset},${Prefs.isyaOffset}")
}

fun SettingsPresenter.showPraytimeAdjustments(){
    val progressDialog = Dialog(mContext)
    progressDialog.setContentView(DialogPraytimeAdjustmentsBinding.inflate(LayoutInflater.from(mContext)).apply {
        sliderFajr.value = Prefs.fajrOffset.toFloat()
        textValueFajr.text = "${Prefs.fajrOffset} ${LocaleConstants.MINUTE.locale()}"
        sliderFajr.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            Prefs.fajrOffset = value.toInt()
            textValueFajr.text = "${value.toInt()} ${LocaleConstants.MINUTE.locale()}"
            configureViews()
        })
        sliderDhuhr.value = Prefs.dhuhrOffset.toFloat()
        textValueDhuhr.text = "${Prefs.dhuhrOffset} ${LocaleConstants.MINUTE.locale()}"
        sliderDhuhr.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            Prefs.dhuhrOffset = value.toInt()
            textValueDhuhr.text = "${value.toInt()} ${LocaleConstants.MINUTE.locale()}"
            configureViews()
        })
        sliderAsr.value = Prefs.asrOffset.toFloat()
        textValueAsr.text = "${Prefs.asrOffset} ${LocaleConstants.MINUTE.locale()}"
        sliderAsr.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            Prefs.asrOffset = value.toInt()
            textValueAsr.text = "${value.toInt()} ${LocaleConstants.MINUTE.locale()}"
            configureViews()
        })
        sliderMaghrib.value = Prefs.maghribOffset.toFloat()
        textValueMaghrib.text = "${Prefs.maghribOffset} ${LocaleConstants.MINUTE.locale()}"
        sliderMaghrib.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            Prefs.maghribOffset = value.toInt()
            textValueMaghrib.text = "${value.toInt()} ${LocaleConstants.MINUTE.locale()}"
            configureViews()
        })
        sliderIsya.value = Prefs.isyaOffset.toFloat()
        textValueIsya.text = "${Prefs.isyaOffset} ${LocaleConstants.MINUTE.locale()}"
        sliderIsya.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            Prefs.isyaOffset = value.toInt()
            textValueIsya.text = "${value.toInt()} ${LocaleConstants.MINUTE.locale()}"
            configureViews()
        })
    }.root)
    progressDialog.show()
}