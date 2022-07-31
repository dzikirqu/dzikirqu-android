package com.wagyufari.dzikirqu.ui.bsd.quran.deeplink

import androidx.lifecycle.liveData
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
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