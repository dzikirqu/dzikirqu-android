package com.dzikirqu.android.ui.onboarding.fragments.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.dzikirqu.android.data.Prefs

class OnBoardPraytimeViewModel:ViewModel(){



    var ringFajr = ObservableField(Prefs.ringFajr)
    var ringDhuhr = ObservableField(Prefs.ringDhuhr)
    var ringAsr = ObservableField(Prefs.ringAsr)
    var ringMaghrib = ObservableField(Prefs.ringMaghrib)
    var ringIsya = ObservableField(Prefs.ringIsya)
}

