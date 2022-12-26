package com.dzikirqu.android.ui.main.prayer

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.events.ApplyWindowEvent
import com.dzikirqu.android.model.events.BackEvent
import com.dzikirqu.android.model.events.PrayerTypeEvent
import com.dzikirqu.android.util.prayerTypes
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