package com.wagyufari.dzikirqu.ui.note.composer

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getNoteDao
import com.wagyufari.dzikirqu.databinding.ActivityNoteComposerBinding
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.ui.note.composer.input.*
import com.wagyufari.dzikirqu.util.TextViewUndoRedo
import com.wagyufari.dzikirqu.util.ViewUtils
import com.wagyufari.dzikirqu.util.foreground.NoteBackupWorker
import com.wagyufari.dzikirqu.util.io
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.editor.MarkwonEditor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class NoteComposerActivity : BaseActivity<ActivityNoteComposerBinding, NoteComposerViewModel>(),
    NoteComposerNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_note_composer
    override val viewModel: NoteComposerViewModel by viewModels()

    lateinit var datePickerDialog: DatePickerDialog
    var selectionStart = 0
    var selectionEnd = 0
    lateinit var mTextViewUndoRedo: TextViewUndoRedo
    lateinit var wysiwygEditor: MarkwonEditor

    companion object {

        const val EXTRA_NOTE_ID = "extra_note_id"
        const val EXTRA_IS_COMPOSER = "extra_is_composer"
        const val EXTRA_IS_PREVIEW = "extra_is_preview"
        const val EXTRA_FOLDER_NAME = "extra_folder_name"

        fun newIntent(
            context: Context,
            noteId: Int? = null,
            isComposer: Boolean? = true,
            isPreview: Boolean? = false,
            folderName:String?=null
        ): Intent {
            return Intent(context, NoteComposerActivity::class.java).apply {
                putExtra(EXTRA_NOTE_ID, noteId)
                putExtra(EXTRA_IS_COMPOSER, isComposer)
                putExtra(EXTRA_IS_PREVIEW, isPreview)
                putExtra(EXTRA_FOLDER_NAME, folderName)
            }
        }

        fun NoteComposerActivity.getFolderName():String?{
            return intent.getStringExtra(EXTRA_FOLDER_NAME)
        }

        fun NoteComposerActivity.getNoteId(): Int {
            return intent.getIntExtra(EXTRA_NOTE_ID, -1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewDataBinding.lifecycleOwner = this

        getFolderName()?.let {
            viewModel.folder.value = it
        }

        if (getNoteId() != -1) {
            getNoteDao().getNoteByIds(getNoteId()).observe(this) {
                it?.firstOrNull()?.let { note ->
                    viewModel.setData(note)
                }
                configureViews()
            }
        } else {
            lifecycleScope.launch {
                val id = getNoteDao().putNote(Note().apply {
                    createdDate =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().time)
                    updatedDate =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().time)
                })
                viewModel.note.id = id.toInt()
                configureViews()
            }
        }
    }

    fun configureViews() {
        viewDataBinding.composeView.setContent {
            metaData()
        }
        configureDatePicker()
        NoteInputLogicDelegate()
        NoteComposeLogicDelegate()
        viewDataBinding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val diff: Int = viewDataBinding.scrollView.getChildAt(0).bottom - (viewDataBinding.scrollView.height + viewDataBinding.scrollView.scrollY)
            viewDataBinding.scrollDown.isVisible = diff != 0

            viewDataBinding.divider.isVisible = viewDataBinding.scrollView.scrollY > 0
            if (viewDataBinding.scrollView.scrollY > 0){
                viewDataBinding.appbar.setBackgroundColor(ContextCompat.getColor(this, R.color.neutral_100))
            } else{
                viewDataBinding.appbar.setBackgroundColor(ContextCompat.getColor(this, R.color.neutral_0))
            }

        }
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding.editor.textSize = Prefs.notesTextSize
        viewDataBinding.editor.setText(viewDataBinding.editor.text.toString())
    }

    fun Context.showDeleteConfirmationDialog(onPositive: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(LocaleConstants.DELETE_NOTE_Q.locale())
            .setMessage(LocaleConstants.ARE_YOU_SURE_YOU_WANT_TO_DELETE_THIS_NOTE.locale())
            .setPositiveButton(LocaleConstants.YES.locale()) { dialog, which ->
                onPositive.invoke()
            }
            .setNegativeButton(LocaleConstants.NO.locale()) { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    override fun onBackPressed() {
        if (viewDataBinding.scrollView.scrollY != 0){
            viewDataBinding.scrollView.fullScroll(View.FOCUS_UP)
        } else{
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateNote()
    }

    fun updateNote(){
        io {
            viewModel.updateNoteData()
            val note = viewModel.note.apply {
                if (viewModel.note.content != viewModel.initialNote.content) {
                    updatedDate =
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().time)
                }
            }
            getNoteDao().updateNote(note)
            NoteBackupWorker.start(this, note)
        }
        Prefs.suspendedNoteId = -1
        Prefs.suspendedNoteContent = ""
    }

    fun configureDatePicker() {
        val calendar = Calendar.getInstance().apply {
            time = viewModel.date.value
        }
        datePickerDialog = DatePickerDialog(this,
            { datePicker, i, i2, i3 ->
                viewModel.date.value =
                    SimpleDateFormat("dd/MM/yyyy").parse("${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}")
                configureViews()
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
    }

    override fun onNoteEditorEvent() {
        io {
            viewModel.updateNoteData()
            Prefs.suspendedNoteId = viewModel.note.id ?: -1
            Prefs.suspendedNoteContent = viewModel.note.content ?: ""
        }
    }

    override fun onNoteInsertEvent(data: String) {
        handleInsert(data)
    }

}
