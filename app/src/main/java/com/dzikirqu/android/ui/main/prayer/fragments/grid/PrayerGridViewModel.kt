package com.dzikirqu.android.ui.main.prayer.fragments.grid

import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
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