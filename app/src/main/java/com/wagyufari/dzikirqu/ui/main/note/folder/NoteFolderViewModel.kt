package com.wagyufari.dzikirqu.ui.main.note.folder


import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.ApiService
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getNoteDao
import com.wagyufari.dzikirqu.data.room.dao.getNotePropertyDao
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.NoteProperty
import com.wagyufari.dzikirqu.model.NoteSortBy
import com.wagyufari.dzikirqu.model.events.ApplyWindowEvent
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import com.wagyufari.dzikirqu.model.request.toRequestModel
import com.wagyufari.dzikirqu.model.response.toNote
import com.wagyufari.dzikirqu.ui.main.note.personal.deletedFolderId
import com.wagyufari.dzikirqu.util.*
import com.wagyufari.dzikirqu.util.ViewUtils.getProgressDialog
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
