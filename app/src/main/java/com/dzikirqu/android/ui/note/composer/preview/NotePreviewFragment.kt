package com.dzikirqu.android.ui.note.composer.preview

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.FragmentNotePreviewBinding
import com.dzikirqu.android.util.setMarkdown
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotePreviewFragment : BaseFragment<FragmentNotePreviewBinding, NotePreviewViewModel>(){

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_note_preview
    override val viewModel: NotePreviewViewModel by viewModels()

    companion object{

        const val ARG_CONTENT = "arg_content"
        fun newInstance(content:String?=null): NotePreviewFragment {
            return NotePreviewFragment().apply {
                arguments = bundleOf(ARG_CONTENT to content)
            }
        }

        fun NotePreviewFragment.getContent():String?{
            return arguments?.getString(ARG_CONTENT)
        }
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this

        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewDataBinding?.preview?.movementMethod = LinkMovementMethod.getInstance()
        viewDataBinding?.preview?.textSize = Prefs.notesTextSize

        viewModel.content.observe(viewLifecycleOwner){ content->
            viewDataBinding?.preview?.setMarkdown(content.replace("<center>", "<align center> </align>").replace("<end>", "<align end> </align>"))
        }

        viewModel.content.value = getContent() ?: ""

        viewDataBinding?.preview?.isClickable = true
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding?.preview?.textSize = Prefs.notesTextSize
    }

}