package com.dzikirqu.android.ui.dailyreminder

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.QuranLastRead
import com.dzikirqu.android.model.events.MainTabEvent
import com.dzikirqu.android.model.events.MainTabType
import com.dzikirqu.android.model.events.MenuEvent
import com.dzikirqu.android.util.PraytimeWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyReminderViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<DailyReminderNavigator>(dataManager) {

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