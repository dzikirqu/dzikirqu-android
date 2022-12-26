package com.dzikirqu.android.ui.main.note.folder


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.model.NoteSortBy
import com.dzikirqu.android.model.events.ApplyWindowEvent
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.main.note.personal.deletedFolderId
import com.dzikirqu.android.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteFolderViewModel @Inject constructor(
    dataManager: AppDataManager,
) :
    BaseViewModel<NoteFolderNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when (obj) {
            is SettingsEvent -> {
                sortBy.value = Prefs.noteSortBy
                isSortAsc.value = Prefs.isNoteSortAsc
            }
            is ApplyWindowEvent -> {
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }


    val statusBarHeight = mutableStateOf(0)
    var profilePicture = MutableLiveData("")

    val sortBy = MutableLiveData(Prefs.noteSortBy)
    val isSortAsc = MutableLiveData(Prefs.isNoteSortAsc)
    var selectedFolder: MutableLiveData<String?> = MutableLiveData()

    val notes = Transformations.switchMap(TripleTrigger(sortBy, isSortAsc, selectedFolder)) {
        if (it.third == deletedFolderId) {
            dataManager.mNoteDatabase.noteDao().getDeletedNote()
        } else {
            it.third?.let{
                dataManager.mNoteDatabase.noteDao().getNoteByFolder(it)
            }?: kotlin.run {
                dataManager.mNoteDatabase.noteDao().getNotes()
            }
        }
    }

    fun sort(notes:List<Note>):List<Note>{
        return when(sortBy.value){
            NoteSortBy.UPDATED_DATE -> {
                if (isSortAsc.value == true) notes.sortedBy { it.getUpdatedDateObject()?.time } else notes.sortedByDescending { it.getUpdatedDateObject()?.time }
            }
            NoteSortBy.CREATED_DATE -> {
                if (isSortAsc.value == true) notes.sortedBy { it.getDateObject()?.time } else notes.sortedByDescending { it.getDateObject()?.time }
            }
            else -> {
                if (isSortAsc.value == true) notes.sortedByDescending { it.title } else notes.sortedBy { it.title }
            }
        }
    }
}
