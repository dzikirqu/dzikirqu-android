package com.wagyufari.dzikirqu.ui.main.quran

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.SeparatorItem
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.room.dao.SurahDao
import com.wagyufari.dzikirqu.model.events.ApplyWindowEvent
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import com.wagyufari.dzikirqu.util.FileUtils
import com.wagyufari.dzikirqu.util.LocaleProvider
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