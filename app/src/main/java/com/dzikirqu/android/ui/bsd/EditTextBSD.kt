package com.dzikirqu.android.ui.bsd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dzikirqu.android.databinding.BsdEditTextBinding
import com.dzikirqu.android.model.*
import com.dzikirqu.android.util.main
import com.dzikirqu.android.util.thumb
import com.dzikirqu.android.util.verticalSpacer
import kotlinx.coroutines.launch

class EditTextBSD : BottomSheetDialogFragment() {

    lateinit var mBinding: BsdEditTextBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = BsdEditTextBinding.inflate(inflater)
        return mBinding.root
    }

    companion object {
        const val ARG_NOTE_PROPERTY = "arg_note_property"

        fun newInstance(property:NoteProperty): EditTextBSD {
            return EditTextBSD().apply {
                arguments = bundleOf(ARG_NOTE_PROPERTY to property)
            }
        }


        fun EditTextBSD.getNoteProperty():NoteProperty?{
            return arguments?.getParcelable(ARG_NOTE_PROPERTY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.composeView.setContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                verticalSpacer(height = 16)
                thumb()
            }
        }

        getNoteProperty()?.let {
            mBinding.edit.hint = it.type.name
            mBinding.edit.setText(it.content)
            mBinding.edit.setSelection(it.content.length)
            mBinding.edit.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    lifecycleScope.launch {
                        when(it.type){
                            NotePropertyType.Date -> TODO()
                            NotePropertyType.Presenter -> it.updatePresenter(requireActivity(), mBinding.edit.text.toString())
                            NotePropertyType.Location -> it.updateLocation(requireActivity(), mBinding.edit.text.toString())
                            NotePropertyType.Tag -> TODO()
                            NotePropertyType.Folder -> it.updateFolder(requireActivity(), mBinding.edit.text.toString())
                        }
                        main {
                            dismiss()
                        }
                    }
                }
                false
            }

        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

}