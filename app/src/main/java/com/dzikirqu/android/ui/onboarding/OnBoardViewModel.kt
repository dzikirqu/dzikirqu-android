package com.dzikirqu.android.ui.onboarding

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.room.dao.*
import com.dzikirqu.android.model.events.OnBoardEvent
import com.dzikirqu.android.model.events.OnBoardType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnBoardViewModel @Inject constructor(
    dataManager: AppDataManager,
    val bookDao: PrayerBookDao,
    val ayahDao: AyahDao,
    val surahDao: SurahDao,
    val prayerDao: PrayerDao,
    val prayerDataDao: PrayerDataDao
) :
    BaseViewModel<OnBoardNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        if(obj is OnBoardEvent){
            when(obj.type){
                OnBoardType.LANGUAGE -> page.value = 0
                OnBoardType.LOCATION -> page.value = 1
                OnBoardType.PRAYTIME -> page.value = 2
            }
        }
    }

    val page = MutableLiveData(0)

}

















