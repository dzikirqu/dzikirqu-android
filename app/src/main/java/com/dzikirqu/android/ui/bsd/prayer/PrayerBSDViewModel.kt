package com.dzikirqu.android.ui.bsd.prayer

import androidx.databinding.ObservableField
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.PrayerBook
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