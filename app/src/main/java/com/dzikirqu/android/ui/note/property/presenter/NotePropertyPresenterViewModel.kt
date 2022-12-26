package com.dzikirqu.android.ui.note.property.presenter

import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePropertyPresenterViewModel @Inject constructor(dataManager: AppDataManager): BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {

    }

    val presenters = dataManager.mNoteDatabase.notePropertyDao().getNoteProperties()
}