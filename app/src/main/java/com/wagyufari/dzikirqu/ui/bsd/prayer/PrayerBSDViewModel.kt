package com.wagyufari.dzikirqu.ui.bsd.prayer

import androidx.databinding.ObservableField
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.PrayerBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PrayerBSDNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val book = ObservableField<PrayerBook>()
    fun prayer(bookId:String) = dataManager.getPrayer(bookId)

}