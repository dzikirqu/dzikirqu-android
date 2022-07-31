package com.wagyufari.dzikirqu.ui.bsd.prayerbook

import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.ui.bsd.prayer.PrayerBSDNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerBookBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PrayerBSDNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val books = dataManager.getBooks()

}