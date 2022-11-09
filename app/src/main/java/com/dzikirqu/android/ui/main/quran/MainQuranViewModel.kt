package com.dzikirqu.android.ui.main.quran

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.SeparatorItem
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.room.dao.SurahDao
import com.dzikirqu.android.model.events.ApplyWindowEvent
import com.dzikirqu.android.model.events.NoteInsertEvent
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.util.FileUtils
import com.dzikirqu.android.util.LocaleProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainQuranViewModel @Inject constructor(
    dataManager: AppDataManager,
    val surahDao: SurahDao
) :
    BaseViewModel<MainQuranNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SettingsEvent->{
                navigator?.onSettingsEvent()
            }
            is NoteInsertEvent->{
                navigator?.onNotePropertySelectedEvent()
            }
            is ApplyWindowEvent->{
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }

    val lastReadString = MutableLiveData(LocaleProvider.getString(LocaleConstants.NOT_SET))

    val surahId: MutableLiveData<Int?> = MutableLiveData()
    val verseId: MutableLiveData<Int?> = MutableLiveData()

    fun getArabicCalligraphy(mContext: Context, surah: Int): String {
        return Gson().fromJson<ArrayList<SeparatorItem>>(
            FileUtils.getJsonStringFromAssets(
                mContext,
                "json/quran/paged/separator.json"
            ), object :
                TypeToken<ArrayList<SeparatorItem>>() {}.type
        ).filter { it.surah == surah }.firstOrNull()?.unicode ?: ""
    }

}