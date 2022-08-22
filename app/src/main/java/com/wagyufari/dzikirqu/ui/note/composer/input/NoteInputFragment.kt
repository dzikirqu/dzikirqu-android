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

        }
    }

//    override fun onNoteFocusEvent(focus: Boolean) {
//        if (!focus) {
//            viewDataBinding?.editor?.clearFocus()
//            viewDataBinding?.editor?.hideKeyboard()
//        }
//    }
//
//    override fun onFormatEvent(format: NoteFormat) {
//        when (format) {
//            NoteFormat.Bold -> {
//                handleBoldButton()
//            }
//            NoteFormat.Italic -> {
//                handleItalicButton()
//            }
//        }
//    }
//
//    override fun onNoteInsertEvent(url: String) {
//        handleInsert(url)
//    }
//
//    override fun onSettingsEvent() {
//        super.onSettingsEvent()
//        viewDataBinding?.editor?.textSize = Prefs.notesTextSize
//        viewDataBinding?.editor?.setText(viewDataBinding?.editor?.text.toString())
//    }

