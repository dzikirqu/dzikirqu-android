package com.dzikirqu.android.ui.note.composer.input

import android.content.Intent
import android.util.DisplayMetrics
import com.dzikirqu.android.R
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import com.dzikirqu.android.databinding.PopupNoteComposerBinding
import com.dzikirqu.android.model.delete
import com.dzikirqu.android.model.restore
import com.dzikirqu.android.model.share
import com.dzikirqu.android.ui.bsd.settings.SettingsBSD
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants
import com.dzikirqu.android.ui.note.composer.NoteComposerActivity
import com.dzikirqu.android.ui.note.detail.NotePreviewBSD
import com.dzikirqu.android.ui.note.property.folder.NotePropertyFolderActivity
import com.dzikirqu.android.ui.note.property.location.NotePropertyLocationActivity
import com.dzikirqu.android.ui.note.property.presenter.NotePropertyPresenterActivity
import com.dzikirqu.android.ui.note.property.tag.NotePropertyTagActivity
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.getLocale
import com.dzikirqu.android.util.verticalSpacer
import java.text.SimpleDateFormat
import java.util.*

fun NoteComposerActivity.NoteComposeLogicDelegate(){
    viewDataBinding.appbar.setContent {
        AppBar()
    }
    viewDataBinding.composeView.setContent {
        metaData()
    }
}

@Composable
fun NoteComposerActivity.metaData() {

    Column(Modifier.animateContentSize()) {
        NoteMetadataView(
            isPreview = false,
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
                startActivity(Intent(this@metaData,
                    NotePropertyPresenterActivity::class.java))
            },
            location = viewModel.location.value,
            onLocationClicked = {
                startActivity(Intent(this@metaData,
                    NotePropertyLocationActivity::class.java))
            },
            tags = viewModel.tags.toHashSet().toCollection(arrayListOf()),
            onTagClicked = {
                startActivity(NotePropertyTagActivity.newIntent(this@metaData,
                    viewModel.tags.toHashSet().toCollection(arrayListOf())))
            },
            folder = viewModel.folder.value,
            onFolderClicked = {
                startActivity(Intent(this@metaData,
                    NotePropertyFolderActivity::class.java))
            }
        )
        verticalSpacer(height = 8)
    }
}

@Composable
fun NoteMetadataView(
    isPreview: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    subtitle: String,
    onSubtitleChange: (String) -> Unit,
    date: Date,
    onDateClicked: () -> Unit,
    presenter: String,
    onPresenterClicked: () -> Unit,
    location: String,
    onLocationClicked: () -> Unit,
    tags: List<String>,
    onTagClicked: () -> Unit,
    folder: String,
    onFolderClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp)
    ) {
        if (isPreview) {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontFamily = lato,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        id = R.color.textPrimary
                    )
                )
            }
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    fontFamily = lato,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(
                        id = R.color.textSecondary
                    )
                )
            }
        } else {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = onTitleChange,
                decorationBox = { innerTextField ->
                    BasicTextFieldHint(
                        modifier = Modifier,
                        text = title,
                        hint = "Judul Kajian",
                        fontSize = 20
                    )
                    innerTextField()
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lato,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        id = R.color.textPrimary
                    )
                )
            )
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = subtitle,
                onValueChange = onSubtitleChange,
                decorationBox = { innerTextField ->
                    BasicTextFieldHint(
                        modifier = Modifier,
                        text = subtitle,
                        hint = "Subjudul Kajian",
                        fontSize = 16
                    )
                    innerTextField()
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = lato,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(
                        id = R.color.textSecondary
                    )
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Column(Modifier.fillMaxWidth()) {
            NotePropertyView(
                modifier = Modifier.clickable {
                    if (!isPreview) onDateClicked()
                },
                title = "Tanggal",
                image = com.dzikirqu.android.R.drawable.ic_calendar,
                content = SimpleDateFormat("EEEE, dd MMM yyyy", getLocale()).format(date)
            )
            NotePropertyView(modifier = Modifier.clickable {
                if (!isPreview) onPresenterClicked()
            }, title = "Pemateri", image = R.drawable.ic_person, content = presenter)
            NotePropertyView(modifier = Modifier.clickable {
                if (!isPreview) onLocationClicked()
            }, title = "Lokasi", image = R.drawable.ic_location, content = location)
            NotePropertyView(
                modifier = Modifier.clickable {
                    if (!isPreview) onTagClicked()
                },
                title = "Tags",
                image = R.drawable.ic_tags,
                content = tags.filter { it.isNotBlank() }.map { "#$it" }.joinToString(", ")
            )
            NotePropertyView(
                modifier = Modifier.clickable {
                    if (!isPreview) onFolderClicked()
                },
                title = "Folder",
                image = R.drawable.ic_folder,
                content = folder
            )
        }
        Spacer(Modifier.height(16.dp))
        Divider(Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.neutral_200)))
    }
}



@Composable
fun BasicTextFieldHint(
    modifier: Modifier,
    text: String,
    hint: String,
    fontSize: Int? = 32
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (text.isEmpty()) {
            Text(
                text = hint,
                color = Color.Gray,
                fontSize = fontSize?.sp ?: 32.sp
            )
        }
    }
}

@Composable
fun NotePropertyView(modifier: Modifier, title: String, content: String, image: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(16.dp)
                .height(16.dp),
            painter = painterResource(id = image),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colorResource(id = com.dzikirqu.android.R.color.textTertiary))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "$title:",
            color = colorResource(id = com.dzikirqu.android.R.color.textTertiary),
            fontFamily = lato,
            fontSize = 14.sp
        )
        Spacer(Modifier.width(4.dp))
        Text(
            if (content.isBlank()) "-" else content,
            color = colorResource(id = R.color.textPrimary),
            fontFamily = lato,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun NoteComposerActivity.AppBar(){
    val rightButtonImage = mutableListOf<Int>()
    val rightButtonHandler = mutableListOf<(View?) -> Unit>()

    rightButtonImage.add(R.drawable.ic_preview)
    rightButtonImage.add(R.drawable.ic_more_vert)

    rightButtonHandler.add {
        NotePreviewBSD.newInstance(viewModel.content.value).show(supportFragmentManager, "")
    }

    rightButtonHandler.add{
        var popupMenu: PopupWindow? = null
        popupMenu =
            PopupWindow(PopupNoteComposerBinding.inflate(LayoutInflater.from(this))
                .apply {
                    delete.isVisible = viewModel.note.isDeleted == false
                    delete.setOnClickListener {
                        showDeleteConfirmationDialog {
                            viewModel.note.delete(this@AppBar)
                            finish()
                        }
                    }
                    restore.isVisible = viewModel.note.isDeleted == true
                    restore.setOnClickListener {
                        viewModel.note.restore(this@AppBar)
                        finish()
                    }
                    share.setOnClickListener {
                        popupMenu?.dismiss()
                        viewModel.note.share(this@AppBar) { url ->
                            url?.let {
                                ShareCompat.IntentBuilder(this@AppBar)
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


    Appbar(backgroundColor = colorResource(id = android.R.color.transparent))
        .withBackButton()
        .setElevation(0)
        .setRightButton(rightButtonImage = rightButtonImage,
            rightButtonHandler = rightButtonHandler).build()
}