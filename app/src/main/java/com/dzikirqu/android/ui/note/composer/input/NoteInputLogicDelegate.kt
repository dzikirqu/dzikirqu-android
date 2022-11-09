package com.dzikirqu.android.ui.note.composer.input

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.events.NoteEditorEvent
import com.dzikirqu.android.model.events.NoteInsertEvent
import com.dzikirqu.android.ui.bsd.notes.add.NoteDeeplinkTypeBSD
import com.dzikirqu.android.ui.bsd.notes.calligraphy.NoteCalligraphyBSD
import com.dzikirqu.android.ui.bsd.notes.header.NoteHeaderBSD
import com.dzikirqu.android.ui.note.composer.NoteComposerActivity
import com.dzikirqu.android.util.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import com.dzikirqu.android.R

fun NoteComposerActivity.NoteInputLogicDelegate(){
    wysiwygEditor = wysiwygEditor(this)

    viewDataBinding.let { viewDataBinding ->
        mTextViewUndoRedo = TextViewUndoRedo(viewDataBinding.editor)
        viewDataBinding.editor.textSize = Prefs.notesTextSize

        viewDataBinding.editor.onTextChanged {
            if (Prefs.noteWYSIWYGEnabled) {
                viewDataBinding.editor.text?.let { it1 ->
                    wysiwygEditor.process(it1)
                }
            }
            viewDataBinding.editor.textSize = Prefs.notesTextSize
            RxBus.getDefault().send(NoteEditorEvent(it))
        }
        viewDataBinding.editor.setText(viewModel.note.content)
        viewDataBinding.formats.setContent {
            formats()
        }
        viewDataBinding.scrollView.viewTreeObserver.addOnGlobalLayoutListener {
            viewDataBinding.editor.minHeight = viewDataBinding.scrollView.height
        }
        KeyboardVisibilityEvent.setEventListener(
            this
        ) {
            viewDataBinding.scrollView.smoothScrollBy(4, 4)
        }

        viewDataBinding.scrollDown.setOnClickListener {
            viewDataBinding.scrollView.post {
                viewDataBinding.scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }
    }
}


@Composable
fun NoteComposerActivity.formats() {
    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.colorPrimary))
                .clickable {
                    NoteDeeplinkTypeBSD().show(supportFragmentManager, "")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(4.dp))
            Image(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .padding(2.dp),
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(id = R.color.white))
            )
            Spacer(Modifier.height(4.dp))
        }
        verticalSpacer(height = 8)
        LazyRow(
            Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                format(
                    Modifier
                        .clickable {
                            mTextViewUndoRedo.undo()
                            if(viewDataBinding?.editor?.text.toString().isBlank()){
                                mTextViewUndoRedo.redo()
                            }
                        }, R.drawable.ic_undo
                )
                format(
                    Modifier
                        .clickable { mTextViewUndoRedo.redo() }, R.drawable.ic_redo
                )
                format(
                    Modifier
                        .clickable {
                            NoteCalligraphyBSD().show(supportFragmentManager, "")
                        }, R.drawable.ic_rasul
                )
                format(
                    Modifier
                        .clickable {
                            NoteHeaderBSD().show(supportFragmentManager, "")
                        }, R.drawable.ic_format_size
                )
                format(
                    Modifier
                        .clickable {
                            handleBoldButton()
                        }, R.drawable.ic_bold
                )
                format(
                    Modifier
                        .clickable { handleItalicButton() }, R.drawable.ic_itallic
                )
                format(
                    Modifier
                        .clickable { handleBlockQuote() }, R.drawable.ic_quote
                )
                format(
                    Modifier
                        .clickable { RxBus.getDefault().send(NoteInsertEvent("<center>")) },
                    R.drawable.ic_align_center
                )
                format(
                    Modifier
                        .clickable { RxBus.getDefault().send(NoteInsertEvent("<end>")) },
                    R.drawable.ic_align_right
                )

            }
        }
        verticalSpacer(height = 16)
    }
}

@Composable
fun format(modifier: Modifier, image: Int) {
    Image(
        modifier = modifier
            .width(60.dp)
            .height(32.dp)
            .padding(2.dp),
        painter = painterResource(id = image),
        contentDescription = null,
        colorFilter = ColorFilter.tint(colorResource(id = R.color.textTertiary))
    )
}


fun NoteComposerActivity.handleBoldButton() {
    viewDataBinding.let { viewDataBinding ->
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

fun NoteComposerActivity.getEditorCharAt(position:Int):String{
    return viewDataBinding.editor.text?.toString()?.get(position).toString()
}

fun NoteComposerActivity.handleInsert(url:String){
    viewDataBinding.let { viewDataBinding->
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

fun NoteComposerActivity.handleItalicButton() {
    viewDataBinding.let { viewDataBinding ->
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
fun NoteComposerActivity.handleAlignCenter() {
    viewDataBinding.let { viewDataBinding ->
        viewDataBinding.editor.setText(
            StringBuilder(viewDataBinding.editor.text.toString()).insert(
                viewDataBinding.editor.selectionStart,
                "\n<center>"
            ).toString()
        )
        viewDataBinding.editor.setSelection(selectionStart + 11)
    }
}
fun NoteComposerActivity.handleAlignEnd() {
    viewDataBinding.let { viewDataBinding ->
        viewDataBinding.editor.setText(
            StringBuilder(viewDataBinding.editor.text.toString()).insert(
                viewDataBinding.editor.selectionStart,
                "\n<end>"
            ).toString()
        )
        viewDataBinding.editor.setSelection(selectionStart + 8)
    }
}

fun NoteComposerActivity.handleBlockQuote() {
    viewDataBinding.let { viewDataBinding ->
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