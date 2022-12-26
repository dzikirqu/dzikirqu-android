package com.dzikirqu.android.ui.main.quran.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.room.dao.SurahDao


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