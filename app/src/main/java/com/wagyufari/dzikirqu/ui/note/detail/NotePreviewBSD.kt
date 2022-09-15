package com.wagyufari.dzikirqu.ui.note.detail

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wagyufari.dzikirqu.base.FullDialog
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.BsdNotePreviewBinding
import com.wagyufari.dzikirqu.util.setMarkdown
import com.wagyufari.dzikirqu.util.thumb

class NotePreviewBSD : FullDialog() {

    lateinit var mBinding: BsdNotePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = BsdNotePreviewBinding.inflate(inflater)
        return mBinding.root
    }

    companion object{
        const val ARG_TEXT = "arg_text"
        fun newInstance(text:String):NotePreviewBSD{
            return NotePreviewBSD().apply {
                arguments = bundleOf(ARG_TEXT to text)
            }
        }

        fun NotePreviewBSD.getText():String{
            return arguments?.getString(ARG_TEXT).toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.composeView.setContent {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                thumb()
            }
        }

        mBinding.preview.movementMethod = LinkMovementMethod.getInstance()
        mBinding.preview.textSize = Prefs.notesTextSize
        mBinding.preview.setMarkdown(getText().replace("<center>", "<align center> </align>").replace("<end>", "<align end> </align>"))
    }
}