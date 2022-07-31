package com.wagyufari.dzikirqu.model.events

import com.wagyufari.dzikirqu.model.NoteProperty
import com.wagyufari.dzikirqu.model.NotePropertyType

class NoteEditorEvent (val content:String)
class NoteInsertEvent (val text:String)
class NoteEditorFocusEvent (val focus:Boolean)
class NotePropertySelectedEvent(val property:NoteProperty?=null, val properties: List<NoteProperty>?= arrayListOf(), val type: NotePropertyType?=null)

enum class NoteFormat{
    Bold, Italic
}