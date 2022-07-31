package com.wagyufari.dzikirqu.ui.note.composer.input

import android.os.Bundle
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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentNoteInputBinding
import com.wagyufari.dzikirqu.model.events.NoteEditorEvent
import com.wagyufari.dzikirqu.model.events.NoteEditorFocusEvent
import com.wagyufari.dzikirqu.model.events.NoteFormat
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.ui.bsd.notes.add.NoteDeeplinkTypeBSD
import com.wagyufari.dzikirqu.ui.bsd.notes.calligraphy.NoteCalligraphyBSD
import com.wagyufari.dzikirqu.ui.bsd.notes.header.NoteHeaderBSD
import com.wagyufari.dzikirqu.util.*
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.editor.MarkwonEditor
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@AndroidEntryPoint
class NoteInputFragment : BaseFragment<FragmentNoteInputBinding, NoteInputViewModel>(),
    NoteInputNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_note_input
    override val viewModel: NoteInputViewModel by viewModels()

    var selectionStart = 0
    var selectionEnd = 0

    lateinit var mTextViewUndoRedo: TextViewUndoRedo

    lateinit var wysiwygEditor: MarkwonEditor

    companion object {

        const val ARG_CONTENT = "arg_content"
        fun newInstance(content: String? = null): NoteInputFragment {
            return NoteInputFragment().apply {
                arguments = bundleOf(ARG_CONTENT to content)
            }
        }

        fun NoteInputFragment.getContent(): String? {
            return arguments?.getString(ARG_CONTENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        wysiwygEditor = wysiwygEditor(requireActivity())
        viewModel.initialContent = getContent().toString()



        viewDataBinding?.let { viewDataBinding ->
            mTextViewUndoRedo = TextViewUndoRedo(viewDataBinding.editor)
            viewDataBinding.editor.textSize = Prefs.notesTextSize

            viewDataBinding.editor.onTextChanged {
                if (Prefs.noteWYSIWYGEnabled) {
                    viewDataBinding.editor.text?.let { it1 ->
                        wysiwygEditor.process(it1)
                    }
                }
                RxBus.getDefault().send(NoteEditorEvent(it))
            }
            viewDataBinding.editor.setText(getContent())
            viewDataBinding.editor.viewTreeObserver.addOnScrollChangedListener {

            }

            viewDataBinding.editor.setOnFocusChangeListener { view, b ->
                RxBus.getDefault().send(NoteEditorFocusEvent(b))
            }

            viewDataBinding.formats.setContent {
                formats()
            }

            viewDataBinding.rootScroll.viewTreeObserver.addOnGlobalLayoutListener {
                viewDataBinding.editor.minHeight = viewDataBinding.rootScroll.height
            }

            setEventListener(
                requireActivity(),
                KeyboardVisibilityEventListener {
                    viewDataBinding.rootScroll.smoothScrollBy(4,4)
                })

            viewDataBinding.rootScroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val diff: Int = viewDataBinding.rootScroll.getChildAt(viewDataBinding.rootScroll.childCount - 1).bottom - (viewDataBinding.rootScroll.height + viewDataBinding.rootScroll.scrollY)
                viewDataBinding.scrollDown.isVisible = diff != 0
            }

            viewDataBinding.scrollDown.setOnClickListener {
                viewDataBinding.rootScroll.post {
                    viewDataBinding.rootScroll.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    override fun onNoteFocusEvent(focus: Boolean) {
        if (!focus) {
            viewDataBinding?.editor?.clearFocus()
            viewDataBinding?.editor?.hideKeyboard()
        }
    }

    override fun onFormatEvent(format: NoteFormat) {
        when (format) {
            NoteFormat.Bold -> {
                handleBoldButton()
            }
            NoteFormat.Italic -> {
                handleItalicButton()
            }
        }
    }

    override fun onNoteInsertEvent(url: String) {
        handleInsert(url)
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding?.editor?.textSize = Prefs.notesTextSize
        viewDataBinding?.editor?.setText(viewDataBinding?.editor?.text.toString())
    }

    @Composable
    fun formats() {
        Column {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.colorPrimary))
                    .clickable {
                        NoteDeeplinkTypeBSD().show(childFragmentManager, "")
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
                                NoteCalligraphyBSD().show(childFragmentManager, "")
                            }, R.drawable.ic_rasul
                    )
                    format(
                        Modifier
                            .clickable {
                                NoteHeaderBSD().show(childFragmentManager, "")
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


}