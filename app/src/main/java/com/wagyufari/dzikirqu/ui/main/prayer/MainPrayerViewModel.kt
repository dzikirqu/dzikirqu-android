package com.wagyufari.dzikirqu.ui.main.prayer

import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.events.ApplyWindowEvent
import com.wagyufari.dzikirqu.model.events.BackEvent
import com.wagyufari.dzikirqu.model.events.PrayerTypeEvent
import com.wagyufari.dzikirqu.util.prayerTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainPrayerViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<MainPrayerNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is PrayerTypeEvent->{
                pagePosition.value = prayerTypes.indexOfFirst { it.title == obj.type.title } + 1
            }
            is BackEvent->{
                navigator?.onBackEvent()
            }
            is ApplyWindowEvent->{
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }

    var pagePosition = MutableLiveData(0)

}