package com.dzikirqu.android.ui.bsd.quran.deeplink

import androidx.lifecycle.liveData
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.events.NoteInsertEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuranDeeplinkViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<QuranDeeplinkNavigator>(dataManager) {

    val surah = liveData {
        emit(dataManager.mPersistenceDatabase.surahDao().getSurah())
    }

    override fun onEvent(obj: Any) {
        if (obj is NoteInsertEvent){
            navigator?.onDeeplinkEvent()
        }
    }

}