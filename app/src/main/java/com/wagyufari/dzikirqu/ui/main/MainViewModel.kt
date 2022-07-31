package com.wagyufari.dzikirqu.ui.main

import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.model.events.MainTabEvent
import com.wagyufari.dzikirqu.model.events.MainTabType
import com.wagyufari.dzikirqu.model.events.MenuEvent
import com.wagyufari.dzikirqu.util.PraytimeWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<MainNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is MainTabEvent-> tab.value = obj.type
            is QuranLastRead-> {
                CoroutineScope(IO).launch {
                    if (obj.surah == 0) return@launch
                    if (obj.isSavedFromPage == true){
                        dataManager.mPersistenceDatabase.quranLastReadDao().deleteSavedFromPage()
                    }
                    dataManager.mPersistenceDatabase.quranLastReadDao().putQuranLastRead(obj)
                    PraytimeWidget.update(dataManager.mContext)
                }
            }
            is MenuEvent->{
                CoroutineScope(IO).launch {
                    dataManager.mPersistenceDatabase.quranLastReadDao().putQuranLastRead(Prefs.quranLastRead)
                }
            }
        }
    }

    val tab = MutableLiveData(MainTabType.HOME)


}