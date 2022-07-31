package com.wagyufari.dzikirqu.ui.note.composer.input

import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.model.events.NoteFormat

interface NoteInputNavigator:BaseNavigator {
    fun onFormatEvent(format:NoteFormat)
    fun onNoteFocusEvent(focus:Boolean)
    fun onNoteInsertEvent(url:String)
}