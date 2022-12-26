package com.dzikirqu.android.util.binding

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter

object EditTextBinding{

    @BindingAdapter("actionDone")
    @JvmStatic
    fun bindActionDone(editText: EditText, runnable: ()->Unit) {
        editText.setOnEditorActionListener { v, actionId, _ ->
            if (editText.text.toString().isEmpty().not()) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    runnable.invoke()
                }
            }
            false
        }
    }

}