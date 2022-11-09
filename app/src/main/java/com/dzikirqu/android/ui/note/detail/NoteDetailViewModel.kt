package com.dzikirqu.android.ui.note.detail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.model.events.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class NoteDetailViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when (obj) {
            is SettingsEvent->{
                navigator?.onSettingsEvent()
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


    fun setData(note:Note){
        title.value = note.title.toString()
        subtitle.value = note.subtitle.toString()
        content.value = note.content.toString()
        presenter.value = note.speaker?.name.toString()
        date.value = dateFormat.parse(note.date)
        location.value = note.location?.name.toString()
        folder.value = note.folder.toString()
        tags.addAll(note.tags ?: arrayListOf())
    }

}
