package com.wagyufari.dzikirqu.ui.note.composer.preview

import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.events.NoteEditorEvent
import com.wagyufari.dzikirqu.model.events.SettingsEvent
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