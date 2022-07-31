package com.wagyufari.dzikirqu.ui.main.note

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.ui.main.note.personal.NotePersonalFragment
import com.wagyufari.dzikirqu.ui.note.composer.NoteComposerActivity
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.horizontalSpacer

@Composable
fun NotePersonalFragment.ItemNote(note: Note) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = colorResource(id = R.color.card)) {
        Column(Modifier) {
            Box(Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        startActivity(NoteComposerActivity.newIntent(requireActivity(),
                            note.id,
                            false))
                    },
                    indication = rememberRipple()
                )) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Card(Modifier
                            .height(60.dp)
                            .width(52.dp),
                            elevation = 2.dp,
                            shape = RoundedCornerShape(4.dp),
                            backgroundColor = colorResource(id = R.color.neutral_100)) {
                            Text(modifier = Modifier.padding(8.dp),
                                text = note.content.toString(),
                                fontFamily = lato,
                                fontSize = 2.sp,
                                color = colorResource(id = R.color.textPrimary))
                        }
                        horizontalSpacer(width = 16.dp)
                        Column(Modifier.weight(1f)) {
                            if (note.title?.isNotBlank() == true) {
                                Text(text = note.title.toString(),
                                    fontFamily = lato,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(
                                        id = R.color.textPrimary), maxLines = 1)
                            }
                            if (note.subtitle?.isNotBlank() == true) {
                                Text(text = note.subtitle.toString(),
                                    fontFamily = lato,
                                    fontSize = 14.sp,
                                    color = colorResource(
                                        id = R.color.neutral_600), maxLines = 1)
                            }
                            if (note.content?.isNotBlank() == true) {
                                Text(text = note.content.toString().replace("\n", ""),
                                    fontFamily = lato,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    color = colorResource(
                                        id = R.color.neutral_500))
                            }

                        }
                    }

                }
            }
        }

    }
}