package com.dzikirqu.android.ui.note.composer.input

import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteInputViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<NoteInputNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    var initialContent = ""

}