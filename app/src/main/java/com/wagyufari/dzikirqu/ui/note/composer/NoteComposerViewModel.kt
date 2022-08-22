package com.wagyufari.dzikirqu.ui.note.composer

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.Location
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.NotePropertyType
import com.wagyufari.dzikirqu.model.Speaker
import com.wagyufari.dzikirqu.model.events.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class NoteComposerViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<NoteComposerNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when (obj) {
            is NoteInsertEvent->{
                navigator?.onNoteInsertEvent(obj.text)
            }
            is NotePropertySelectedEvent -> {
                when (obj.property?.type) {
                    NotePropertyType.Presenter -> {
                        presenter.value = obj.property.content
                    }
                    NotePropertyType.Location -> {
                        location.value = obj.property.content
                    }
                    NotePropertyType.Folder -> {
                        folder.value = obj.property.content
                    }
                }

                when(obj.type){
                    NotePropertyType.Tag->{
                        tags.clear()
                        tags.addAll(obj.properties?.map { it.content }?.toHashSet()?.toCollection(
                            arrayListOf()) ?: arrayListOf())
                    }
                }
            }
            is SettingsEvent->{
                navigator?.onSettingsEvent()
            }
            is NoteEditorEvent->{
                navigator?.onNoteEditorEvent()
                content.value = obj.content
            }
        }
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    val title = mutableStateOf("")
    val subtitle = mutableStateOf("")
    val content = mutableStateOf("")
    val presenter = mutableStateOf("")
    val date = mutableStateOf(Calendar.getInstance().time)
    val location = mutableStateOf("")
    val tags = mutableStateListOf<String>()
    val folder = mutableStateOf("")
    val isPublic = mutableStateOf(false)

    var initialNote = Note()
    var note = Note()

    var isEditing = MutableLiveData(true)

    fun updateNoteData() {
        note.apply {
            this.title = this@NoteComposerViewModel.title.value
            this.subtitle = this@NoteComposerViewModel.subtitle.value
            this.content = this@NoteComposerViewModel.content.value
            this.date = this@NoteComposerViewModel.dateFormat.format(this@NoteComposerViewModel.date.value)
            this.speaker = Speaker(name = this@NoteComposerViewModel.presenter.value)
            this.location = Location(name = this@NoteComposerViewModel.location.value)
            this.tags = this@NoteComposerViewModel.tags.toCollection(arrayListOf())
            this.folder = this@NoteComposerViewModel.folder.value
            this.isPublic = this@NoteComposerViewModel.isPublic.value
        }
    }

    fun setData(note:Note){
        this@NoteComposerViewModel.note = note
        this@NoteComposerViewModel.initialNote = note.copy()
        title.value = note.title.toString()
        subtitle.value = note.subtitle.toString()
        content.value = note.content.toString()
        presenter.value = note.speaker?.name.toString()
        try{
            date.value = dateFormat.parse(note.date)
        } catch (e:Exception){
            date.value = Calendar.getInstance().time
        }
        location.value = note.location?.name.toString()
        folder.value = note.folder.toString()
        tags.addAll(note.tags?.toHashSet()?.toCollection(arrayListOf()) ?: arrayListOf())
        isPublic.value = note.isPublic == true
    }

}
