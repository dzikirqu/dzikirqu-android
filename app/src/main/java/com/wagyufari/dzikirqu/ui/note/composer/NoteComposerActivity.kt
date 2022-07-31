package com.wagyufari.dzikirqu.ui.note.composer

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getNoteDao
import com.wagyufari.dzikirqu.databinding.ActivityNoteComposerBinding
import com.wagyufari.dzikirqu.databinding.PopupNoteComposerBinding
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.delete
import com.wagyufari.dzikirqu.model.events.NoteEditorFocusEvent
import com.wagyufari.dzikirqu.model.restore
import com.wagyufari.dzikirqu.model.share
import com.wagyufari.dzikirqu.ui.adapters.FragmentPagerAdapter
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsConstants
import com.wagyufari.dzikirqu.ui.note.composer.input.NoteInputFragment
import com.wagyufari.dzikirqu.ui.note.composer.metadata.NoteMetadataView
import com.wagyufari.dzikirqu.ui.note.composer.preview.NotePreviewFragment
import com.wagyufari.dzikirqu.ui.note.property.folder.NotePropertyFolderActivity
import com.wagyufari.dzikirqu.ui.note.property.location.NotePropertyLocationActivity
import com.wagyufari.dzikirqu.ui.note.property.presenter.NotePropertyPresenterActivity
import com.wagyufari.dzikirqu.ui.note.property.tag.NotePropertyTagActivity
import com.wagyufari.dzikirqu.util.Appbar
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.foreground.NoteBackupWorker
import com.wagyufari.dzikirqu.util.io
import com.wagyufari.dzikirqu.util.verticalSpacer
import dagger.hilt.android.AndroidEntryPoint
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

        fun NoteComposerActivity.isComposer(): Boolean {
            return intent.getBooleanExtra(EXTRA_IS_COMPOSER, false)
        }

        fun NoteComposerActivity.isPreview(): Boolean {
            return intent.getBooleanExtra(EXTRA_IS_PREVIEW, false)
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
        configurePager()
        configureDatePicker()
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

    @Composable
    fun metaData() {
        var isExpanded by remember { mutableStateOf(isComposer()) }

        val rightButtonImage = mutableListOf<Int>()
        val rightButtonHandler = mutableListOf<(View?) -> Unit>()

        if (!isComposer()){
            rightButtonImage.add(R.drawable.ic_info)
            rightButtonHandler.add{
                isExpanded = !isExpanded
            }
        }

        rightButtonImage.add(R.drawable.ic_more_vert)
        rightButtonHandler.add{
            var popupMenu: PopupWindow? = null
            popupMenu =
                PopupWindow(PopupNoteComposerBinding.inflate(LayoutInflater.from(this@NoteComposerActivity))
                    .apply {
                        delete.isVisible = viewModel.note.isDeleted == false
                        delete.setOnClickListener {
                            showDeleteConfirmationDialog {
                                viewModel.note.delete(this@NoteComposerActivity)
                                finish()
                            }
                        }
                        restore.isVisible = viewModel.note.isDeleted == true
                        restore.setOnClickListener {
                            viewModel.note.restore(this@NoteComposerActivity)
                            finish()
                        }
                        share.setOnClickListener {
                            popupMenu?.dismiss()
                            viewModel.note.share(this@NoteComposerActivity) { url ->
                                url?.let {
                                    ShareCompat.IntentBuilder(this@NoteComposerActivity)
                                        .setType("text/plain")
                                        .setChooserTitle("Share")
                                        .setText(it)
                                        .startChooser()
                                }
                            }
                        }
                        settings.setOnClickListener {
                            SettingsBSD(arrayListOf(SettingsConstants.NOTES)).show(
                                supportFragmentManager,
                                null
                            )
                            popupMenu?.dismiss()
                        }
                        checkboxPublic.isChecked = viewModel.isPublic.value
                        checkboxPublic.setOnCheckedChangeListener { compoundButton, b ->
                            viewModel.isPublic.value = b
                            updateNote()
                        }
                    }.root,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    true)
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            popupMenu.showAtLocation(viewDataBinding.composeView, Gravity.NO_GRAVITY,displayMetrics.widthPixels,0)
        }

        Column(Modifier.animateContentSize()) {
            val appbar = Appbar(backgroundColor = colorResource(id = android.R.color.transparent))
                .withBackButton()
                .setTitle(if (isExpanded) "" else viewModel.title.value)
                .setElevation(0)
                .setRightButton(rightButtonImage = rightButtonImage,
                    rightButtonHandler = rightButtonHandler)

            appbar.build()

            if (isExpanded) {
                NoteMetadataView(
                    isPreview = isPreview(),
                    title = viewModel.title.value,
                    onTitleChange = {
                        viewModel.title.value = it
                    },
                    subtitle = viewModel.subtitle.value,
                    onSubtitleChange = {
                        viewModel.subtitle.value = it
                    },
                    date = viewModel.date.value,
                    onDateClicked = {
                        datePickerDialog.show()
                    },
                    presenter = viewModel.presenter.value,
                    onPresenterClicked = {
                        startActivity(Intent(this@NoteComposerActivity,
                            NotePropertyPresenterActivity::class.java))
                    },
                    location = viewModel.location.value,
                    onLocationClicked = {
                        startActivity(Intent(this@NoteComposerActivity,
                            NotePropertyLocationActivity::class.java))
                    },
                    tags = viewModel.tags.toHashSet().toCollection(arrayListOf()),
                    onTagClicked = {
                        startActivity(NotePropertyTagActivity.newIntent(this@NoteComposerActivity,
                            viewModel.tags.toHashSet().toCollection(arrayListOf())))
                    },
                    folder = viewModel.folder.value,
                    onFolderClicked = {
                        startActivity(Intent(this@NoteComposerActivity,
                            NotePropertyFolderActivity::class.java))
                    }
                )
                verticalSpacer(height = 8)
            }
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

    fun configurePager() {
        val tabTitles = arrayListOf(
            "Write",
            "Preview",
        )
        if (isPreview()) {
            viewDataBinding.pager.adapter =
                FragmentPagerAdapter(
                    this,
                    arrayListOf(
                        NotePreviewFragment.newInstance(viewModel.note.content)
                    )
                )
            TabLayoutMediator(viewDataBinding.tab, viewDataBinding.pager) { tab, position ->
                tab.text = "Preview"
            }.attach()
        } else {
            val fragments: List<Fragment> = arrayListOf(
                NoteInputFragment.newInstance(viewModel.note.content),
                NotePreviewFragment.newInstance(viewModel.note.content)
            )
            viewDataBinding.pager.adapter =
                FragmentPagerAdapter(
                    this,
                    fragments
                )
            TabLayoutMediator(viewDataBinding.tab, viewDataBinding.pager) { tab, position ->
                tab.text = tabTitles[position]
                viewDataBinding.pager.setCurrentItem(1, false)
                if (isComposer()) {
                    viewDataBinding.pager.setCurrentItem(0, true)
                }
            }.attach()
        }
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

    override fun onBackPressed() {
        if (viewDataBinding.motionLayout.currentState == R.id.start) {
            super.onBackPressed()
        } else {
            viewDataBinding.motionLayout.getTransition(R.id.defaultTransition).isEnabled = true
            viewDataBinding.motionLayout.transitionToStart()
            RxBus.getDefault().send(NoteEditorFocusEvent(false))
        }
    }

    override fun onEditorFocusEvent(focus: Boolean) {
        if (focus) {
            viewDataBinding.motionLayout.getTransition(R.id.defaultTransition).isEnabled = false
            viewDataBinding.motionLayout.transitionToEnd()
        }
    }

    override fun onNoteEditorEvent() {
        io {
            viewModel.updateNoteData()
            Prefs.suspendedNoteId = viewModel.note.id ?: -1
            Prefs.suspendedNoteContent = viewModel.note.content ?: ""
        }
    }

}
