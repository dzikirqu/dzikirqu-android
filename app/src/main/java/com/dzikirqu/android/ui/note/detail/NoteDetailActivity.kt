package com.dzikirqu.android.ui.note.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.dzikirqu.android.BR
import com.dzikirqu.android.base.BaseActivity
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.ActivityNoteDetailBinding
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.ui.note.composer.input.NoteMetadataView
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.setMarkdown
import dagger.hilt.android.AndroidEntryPoint
import com.dzikirqu.android.R

@AndroidEntryPoint
class NoteDetailActivity : BaseActivity<ActivityNoteDetailBinding, NoteDetailViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_note_detail
    override val viewModel: NoteDetailViewModel by viewModels()

    companion object {
        const val EXTRA_NOTE = "extra_note"

        fun newIntent(
            context: Context,
            note: Note? = null,
        ): Intent {
            return Intent(context, NoteDetailActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
        }

        fun NoteDetailActivity.getNote(): Note? {
            return intent.getParcelableExtra(EXTRA_NOTE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewDataBinding.lifecycleOwner = this

        getNote()?.let {
            viewModel.setData(it)
            configureViews(it)
        }
    }

    fun configureViews(note: Note) {
        viewDataBinding.composeView.setContent {
            metaData()
        }
        viewDataBinding.preview.movementMethod = LinkMovementMethod.getInstance()
        viewDataBinding.preview.textSize = Prefs.notesTextSize
        viewDataBinding.preview.setMarkdown(note.content.toString())
        viewDataBinding.preview.isClickable = true
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding.preview.textSize = Prefs.notesTextSize
    }

    @Composable
    fun metaData() {
        val rightButtonImage = mutableListOf<Int>()
        val rightButtonHandler = mutableListOf<(View?) -> Unit>()

        Column(Modifier.animateContentSize()) {
            val appbar = Appbar(backgroundColor = colorResource(id = android.R.color.transparent))
                .withBackButton()
                .setElevation(0)
                .setRightButton(rightButtonImage = rightButtonImage,
                    rightButtonHandler = rightButtonHandler)

            appbar.build()

            NoteMetadataView(
                isPreview = true,
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
                },
                presenter = viewModel.presenter.value,
                onPresenterClicked = {
                },
                location = viewModel.location.value,
                onLocationClicked = {
                },
                tags = viewModel.tags,
                onTagClicked = {
                },
                folder = viewModel.folder.value,
                onFolderClicked = {
                }
            )
        }

    }
}