package com.wagyufari.dzikirqu.ui.bsd.notes.main.filter

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseDialog
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.MainNoteFilterBsdBinding
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.thumb
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNoteFilterBSD :
    BaseDialog<MainNoteFilterBsdBinding, MainNoteFilterBSDViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.main_note_filter_bsd
    override val viewModel: MainNoteFilterBSDViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.composeView?.setContent {
            var viewMode by remember { mutableStateOf(Prefs.noteViewMode) }
            var sortBy by remember { mutableStateOf(Prefs.noteSortBy) }
            var isSortAsc by remember { mutableStateOf(Prefs.isNoteSortAsc) }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                thumb()
                Spacer(Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "View Mode",
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = R.color.textSecondary
                    )
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.neutral_100)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    viewMode(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                RxBus
                                    .getDefault()
                                    .send(SettingsEvent())
                                Prefs.noteViewMode = 0
                                viewMode = 0
                            }, R.drawable.ic_staggered, isSelected = viewMode == 0
                    )
                    viewMode(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                RxBus
                                    .getDefault()
                                    .send(SettingsEvent())
                                Prefs.noteViewMode = 1
                                viewMode = 1
                            }, R.drawable.ic_agenda, isSelected = viewMode == 1
                    )
                    viewMode(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                RxBus
                                    .getDefault()
                                    .send(SettingsEvent())
                                Prefs.noteViewMode = 2
                                viewMode = 2
                            }, R.drawable.ic_list, isSelected = viewMode == 2
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sort By",
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = R.color.textSecondary
                    )
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.neutral_100))
                ) {
                    sortBy(
                        modifier = Modifier.clickable {
                            RxBus
                                .getDefault()
                                .send(SettingsEvent())
                            Prefs.noteSortBy = 0
                            sortBy = 0
                        },
                        name = "Updated Date",
                        isSelected = sortBy == 0,
                        isSortAsc = isSortAsc,
                        isSortDirectionClicked = {
                            isSortAsc = !isSortAsc
                            Prefs.isNoteSortAsc = !Prefs.isNoteSortAsc
                        }
                    )
                    sortBy(
                        modifier = Modifier.clickable {
                            RxBus
                                .getDefault()
                                .send(SettingsEvent())
                            Prefs.noteSortBy = 1
                            sortBy = 1
                        },
                        name = "Created Date",
                        isSelected = sortBy == 1,
                        isSortAsc = isSortAsc,
                        isSortDirectionClicked = {
                            isSortAsc = !isSortAsc
                            Prefs.isNoteSortAsc = !Prefs.isNoteSortAsc
                        }
                    )
                    sortBy(
                        modifier = Modifier.clickable {
                            RxBus
                                .getDefault()
                                .send(SettingsEvent())
                            Prefs.noteSortBy = 2
                            sortBy = 2
                        },
                        name = "Title",
                        isSelected = sortBy == 2,
                        isSortAsc = isSortAsc,
                        isSortDirectionClicked = {
                            isSortAsc = !isSortAsc
                            Prefs.isNoteSortAsc = !Prefs.isNoteSortAsc
                        }
                    )
                }
                Spacer(Modifier.height(64.dp))
            }
        }
    }

    @Composable
    fun sortBy(
        modifier: Modifier,
        name: String,
        isSelected: Boolean,
        isSortAsc: Boolean,
        isSortDirectionClicked: () -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(if (isSelected) 8.dp else 0.dp),
            elevation = if (isSelected) 1.dp else 0.dp,
            backgroundColor = colorResource(id = if (isSelected) R.color.neutral_50 else R.color.neutral_100)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(12.dp),
                    text = name,
                    fontFamily = lato,
                    fontSize = 16.sp,
                    color = colorResource(
                        id = R.color.textSecondary
                    )
                )
                if (isSelected) {
                    Row(modifier = Modifier.clickable {
                        isSortDirectionClicked()
                        RxBus
                            .getDefault()
                            .send(SettingsEvent())
                    }, verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                            painter = painterResource(id = if (isSortAsc) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                colorResource(id = R.color.neutral_600)
                            )
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun viewMode(modifier: Modifier, image: Int, isSelected: Boolean) {
        Card(
            modifier = modifier.padding(if (isSelected) 8.dp else 0.dp),
            elevation = if (isSelected) 1.dp else 0.dp,
            backgroundColor = colorResource(id = if (isSelected) R.color.neutral_50 else R.color.neutral_100)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
                Image(
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp),
                    painter = painterResource(id = image),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.neutral_600))
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }

}