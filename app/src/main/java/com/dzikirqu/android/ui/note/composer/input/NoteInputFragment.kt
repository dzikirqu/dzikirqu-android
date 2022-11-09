package com.dzikirqu.android.ui.note.composer.input

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.dzikirqu.android.BR
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentNoteInputBinding
import com.dzikirqu.android.util.*
import dagger.hilt.android.AndroidEntryPoint
import com.dzikirqu.android.R


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

