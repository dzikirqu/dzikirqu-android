package com.wagyufari.dzikirqu.ui.note.composer.metadata

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
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
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.getLocale
import java.text.SimpleDateFormat
import java.util.*

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
            .background(colorResource(id = R.color.neutral_0))
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
                image = R.drawable.ic_calendar,
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
        Spacer(Modifier.height(8.dp))
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
            colorFilter = ColorFilter.tint(colorResource(id = R.color.textTertiary))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "$title:",
            color = colorResource(id = R.color.textTertiary),
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
fun NoteAddProperty(modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colorResource(id = R.color.textTertiary))
        )
        Spacer(Modifier.width(8.dp))
        Text("Add a property", color = colorResource(id = R.color.textTertiary), fontFamily = lato)
    }
}