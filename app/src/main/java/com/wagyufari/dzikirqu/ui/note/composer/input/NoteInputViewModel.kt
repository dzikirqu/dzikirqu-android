package com.wagyufari.dzikirqu.ui.note.composer.input

import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.events.NoteEditorFocusEvent
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteInputViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<NoteInputNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when (obj) {
            is NoteEditorFocusEvent->{
                navigator?.onNoteFocusEvent(obj.focus)
            }
            is SettingsEvent->{
                navigator?.onSettingsEvent()
            }
            is NoteInsertEvent->{
                navigator?.onNoteInsertEvent(obj.text)
            }
        }
    }

    var initialContent = ""

}

fun NoteInputFragment.handleBoldButton() {
    viewDataBinding?.let { viewDataBinding ->
        selectionStart = viewDataBinding.editor.selectionStart
        selectionEnd = viewDataBinding.editor.selectionEnd
        if (viewDataBinding.editor.selectionStart == viewDataBinding.editor.selectionEnd) {
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    "****"
                ).toString()
            )
            viewDataBinding.editor.setSelection(selectionStart + 2)
        } else {
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    "**"
                ).insert(viewDataBinding.editor.selectionEnd + 2, "**").toString()
            )
            viewDataBinding.editor.setSelection(selectionEnd + 2)
        }

    }
}

fun NoteInputFragment.getEditorCharAt(position:Int):String{
    return viewDataBinding?.editor?.text?.toString()?.get(position).toString()
}

fun NoteInputFragment.handleInsert(url:String){
    viewDataBinding?.let { viewDataBinding->
        selectionStart = viewDataBinding.editor.selectionStart
        selectionEnd = viewDataBinding.editor.selectionEnd

        if (url == "<center>" || url == "<end>"){
            if (selectionStart > 1){
                if ((getEditorCharAt(selectionStart-1) + getEditorCharAt(selectionStart-2)) == "\n\n"){
                    viewDataBinding.editor.setText(
                        StringBuilder(viewDataBinding.editor.text.toString()).insert(
                            viewDataBinding.editor.selectionStart,
                            url
                        ).toString()
                    )
                    viewDataBinding.editor.setSelection(selectionStart + url.length)
                } else if(getEditorCharAt(selectionStart-1) == "\n"){
                    viewDataBinding.editor.setText(
                        StringBuilder(viewDataBinding.editor.text.toString()).insert(
                            viewDataBinding.editor.selectionStart,
                            "\n"+url
                        ).toString()
                    )
                    viewDataBinding.editor.setSelection(selectionStart + url.length + 1)
                } else{
                    viewDataBinding.editor.setText(
                        StringBuilder(viewDataBinding.editor.text.toString()).insert(
                            viewDataBinding.editor.selectionStart,
                            "\n\n"+url
                        ).toString()
                    )
                    viewDataBinding.editor.setSelection(selectionStart + url.length + 2)
                }
//                if (!(viewDataBinding.editor.text.toString()[selectionStart-2].toString() == "\n" && viewDataBinding.editor.text.toString()[selectionStart-1].toString() == "\n") && viewDataBinding.editor.text.toString()[selectionStart-1].toString() != "\n"){
//                    viewDataBinding.editor.setText(
//                        StringBuilder(viewDataBinding.editor.text.toString()).insert(
//                            viewDataBinding.editor.selectionStart,
//                            "\n\n"+url
//                        ).toString()
//                    )
//                    viewDataBinding.editor.setSelection(selectionStart + url.length + 2)
//                } else if (viewDataBinding.editor.text.toString()[selectionStart-1].toString() == "\n"){
//                    viewDataBinding.editor.setText(
//                        StringBuilder(viewDataBinding.editor.text.toString()).insert(
//                            viewDataBinding.editor.selectionStart,
//                            "\n"+url
//                        ).toString()
//                    )
//                    viewDataBinding.editor.setSelection(selectionStart + url.length + 1)
//                } else{
//                    viewDataBinding.editor.setText(
//                        StringBuilder(viewDataBinding.editor.text.toString()).insert(
//                            viewDataBinding.editor.selectionStart,
//                            url
//                        ).toString()
//                    )
//                    viewDataBinding.editor.setSelection(selectionStart + url.length)
//                }
            }
        } else{
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    url
                ).toString()
            )
            viewDataBinding.editor.setSelection(selectionStart + url.length)
        }
    }
}

fun NoteInputFragment.handleItalicButton() {
    viewDataBinding?.let { viewDataBinding ->
        selectionStart = viewDataBinding.editor.selectionStart
        selectionEnd = viewDataBinding.editor.selectionEnd
        if (viewDataBinding.editor.selectionStart == viewDataBinding.editor.selectionEnd) {
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    "__"
                ).toString()
            )
            viewDataBinding.editor.setSelection(selectionStart + 1)
        } else {
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    "_"
                ).insert(viewDataBinding.editor.selectionEnd + 1, "_").toString()
            )
            viewDataBinding.editor.setSelection(selectionEnd + 1)
        }
    }
}
fun NoteInputFragment.handleAlignCenter() {
    viewDataBinding?.let { viewDataBinding ->
        viewDataBinding.editor.setText(
            StringBuilder(viewDataBinding.editor.text.toString()).insert(
                viewDataBinding.editor.selectionStart,
                "\n<center>"
            ).toString()
        )
        viewDataBinding.editor.setSelection(selectionStart + 11)
    }
}
fun NoteInputFragment.handleAlignEnd() {
    viewDataBinding?.let { viewDataBinding ->
        viewDataBinding.editor.setText(
            StringBuilder(viewDataBinding.editor.text.toString()).insert(
                viewDataBinding.editor.selectionStart,
                "\n<end>"
            ).toString()
        )
        viewDataBinding.editor.setSelection(selectionStart + 8)
    }
}

fun NoteInputFragment.handleBlockQuote() {
    viewDataBinding?.let { viewDataBinding ->
        selectionStart = viewDataBinding.editor.selectionStart
        selectionEnd = viewDataBinding.editor.selectionEnd
        if (viewDataBinding.editor.selectionStart == viewDataBinding.editor.selectionEnd) {
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    "``"
                ).toString()
            )
            viewDataBinding.editor.setSelection(selectionStart + 1)
        } else {
            viewDataBinding.editor.setText(
                StringBuilder(viewDataBinding.editor.text.toString()).insert(
                    viewDataBinding.editor.selectionStart,
                    "`"
                ).insert(viewDataBinding.editor.selectionEnd + 1, "`").toString()
            )
            viewDataBinding.editor.setSelection(selectionEnd + 1)
        }
    }
}