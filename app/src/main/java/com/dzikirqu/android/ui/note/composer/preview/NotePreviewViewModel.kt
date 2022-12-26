package com.dzikirqu.android.ui.note.composer.preview

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.events.NoteEditorEvent
import com.dzikirqu.android.model.events.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePreviewViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when(obj){
            is NoteEditorEvent->{
                content.value = obj.content
            }
            is SettingsEvent ->{
                navigator?.onSettingsEvent()
            }
        }
    }

    val content = MutableLiveData("")

}