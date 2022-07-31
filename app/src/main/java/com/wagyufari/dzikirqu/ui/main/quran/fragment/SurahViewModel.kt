package com.wagyufari.dzikirqu.ui.main.quran.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.room.dao.SurahDao


import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SurahViewModel @Inject constructor(
    dataManager: AppDataManager,
    val surahDao: SurahDao
) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val isLoading = MutableLiveData(true)

    val surah = liveData {
        emit(dataManager.mPersistenceDatabase.surahDao().getSurah())
    }


}