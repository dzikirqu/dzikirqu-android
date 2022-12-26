package com.dzikirqu.android.ui.note.composer

import com.dzikirqu.android.base.BaseNavigator

interface NoteComposerNavigator:BaseNavigator {
    fun onNoteEditorEvent()
    fun onNoteInsertEvent(data:String)
}