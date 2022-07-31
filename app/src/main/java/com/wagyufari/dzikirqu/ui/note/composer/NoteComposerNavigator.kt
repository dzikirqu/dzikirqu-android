package com.wagyufari.dzikirqu.ui.note.composer

import com.wagyufari.dzikirqu.base.BaseNavigator

interface NoteComposerNavigator:BaseNavigator {
    fun onEditorFocusEvent(focus:Boolean)
    fun onNoteEditorEvent()
}