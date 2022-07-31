package com.wagyufari.dzikirqu.ui.main.prayer.fragments.grid

import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerGridViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PrayerGridNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }



}