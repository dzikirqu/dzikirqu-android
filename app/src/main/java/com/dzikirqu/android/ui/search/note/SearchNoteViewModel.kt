package com.dzikirqu.android.ui.search.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.model.events.SearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchNoteViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SearchEvent->{
                query.value = obj.query
            }
        }
    }

    val query = MutableLiveData("")
    val result = query.switchMap { query->
        liveData {
            if (query.isNullOrBlank().not()){
                val result = hashSetOf<Note>()
                result.addAll(dataManager.mNoteDatabase.noteDao().searchNoteByTitle("%$query%"))
                result.addAll(dataManager.mNoteDatabase.noteDao().searchNoteBySubtitle("%$query%"))
                result.addAll(dataManager.mNoteDatabase.noteDao().searchNoteByContent("%$query%"))
                result.addAll(dataManager.mNoteDatabase.noteDao().searchNoteBySpeaker("%$query%"))
                result.addAll(dataManager.mNoteDatabase.noteDao().searchNoteByLocation("%$query%"))
                result.addAll(dataManager.mNoteDatabase.noteDao().searchNoteByTags("%$query%"))
                emit(result.toList())
            } else{
                emit(arrayListOf())
            }
        }
    }

}