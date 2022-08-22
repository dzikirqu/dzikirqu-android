package com.wagyufari.dzikirqu.ui.note.composer.input

import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteInputViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<NoteInputNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    var initialContent = ""

}