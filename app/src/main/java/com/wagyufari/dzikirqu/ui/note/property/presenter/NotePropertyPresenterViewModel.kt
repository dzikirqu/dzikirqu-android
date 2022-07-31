package com.wagyufari.dzikirqu.ui.note.property.presenter

import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePropertyPresenterViewModel @Inject constructor(dataManager: AppDataManager): BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {

    }

    val presenters = dataManager.mNoteDatabase.notePropertyDao().getNoteProperties()
}