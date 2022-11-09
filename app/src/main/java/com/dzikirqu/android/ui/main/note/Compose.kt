package com.dzikirqu.android.ui.main.note

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.getNotePropertyDao
import com.dzikirqu.android.model.NotePropertyType
import com.dzikirqu.android.model.NoteSortBy
import com.dzikirqu.android.model.events.FolderEvent
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.main.note.personal.NotePersonalFragment
import com.dzikirqu.android.ui.main.note.personal.NotePersonalFragment.Companion.getFolderName
import com.dzikirqu.android.ui.main.note.personal.deletedFolderId
import com.dzikirqu.android.ui.main.note.personal.signIn
import com.dzikirqu.android.ui.main.note.personal.signOut
import com.dzikirqu.android.ui.search.SearchActivity
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.horizontalSpacer
import com.dzikirqu.android.util.verticalSpacer
import com.dzikirqu.android.R

@Composable
fun NotePersonalFragment.appbar() {

    val currentUser = viewModel.currentUser.observeAsState().value

    Column(
        modifier = Modifier,
    ) {
        val rightButtonImage = mutableListOf<Any>()
        val rightButtonImageHandler = mutableListOf<(View?) -> Unit>()
        currentUser?.let {
            rightButtonImage.add(R.drawable.ic_search)
            rightButtonImageHandler.add {
                startActivity(SearchActivity.newNoteIntent(requireActivity()))
            }
            rightButtonImage.add(R.drawable.ic_menu)
            rightButtonImageHandler.add {
                viewDataBinding?.drawerLayout?.openDrawer(viewDataBinding?.drawer!!)
            }
        } ?: kotlin.run {
            rightButtonImage.add(R.drawable.ic_google)
            rightButtonImageHandler.add {
                signIn()
            }
        }

        Appbar(Modifier,
            Modifier.padding(top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp),
            backgroundColor = colorResource(id = android.R.color.transparent)).setTitle(
            getFolderName() ?: LocaleConstants.NOTES.locale(),
        ).withBackButton().setRightButton(
            rightButtonImage = rightButtonImage,
            rightButtonHandler = rightButtonImageHandler).setElevation(0).build()
    }
}

@Composable
fun NotePersonalFragment.drawer() {
    Column {
        with(LocalDensity.current) {
            verticalSpacer(height = viewModel.statusBarHeight.value.toDp())
        }
        AndroidView(factory = {
            ComposeView(it).apply {
                setContent {
                    val currentUser = viewModel.currentUser.observeAsState().value
                    Column(Modifier
                        .clickable {
                            val popupMenu = PopupMenu(requireActivity(), this)
                            popupMenu.menu.add(LocaleConstants.SIGN_OUT.locale())
                            popupMenu.setOnMenuItemClickListener {
                                viewDataBinding?.drawerLayout?.closeDrawer(viewDataBinding?.drawer!!)
                                signOut()
                                true
                            }
                            popupMenu.show()
                        }
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        Image(modifier = Modifier
                            .height(64.dp)
                            .width(64.dp)
                            .clip(CircleShape),
                            painter = rememberAsyncImagePainter(model = currentUser?.photoUrl),
                            contentDescription = null)
                        verticalSpacer(height = 16)
                        Text(text = currentUser?.displayName.toString(),
                            fontFamily = lato,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(
                                id = R.color.textPrimary), maxLines = 1)
                        Row(Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = currentUser?.email.toString(),
                                fontFamily = lato,
                                fontSize = 14.sp,
                                color = colorResource(
                                    id = R.color.textTertiary))
                            Image(modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    colorResource(id = R.color.textTertiary)))
                        }

                    }
                }
            }
        })
        Divider(color = colorResource(id = R.color.neutral_900).copy(0.1f))

        verticalSpacer(height = 16)

        Text(modifier = Modifier.padding(start = 16.dp), text = LocaleConstants.SORT.locale(),
            fontFamily = lato,
            fontSize = 16.sp,
            color = colorResource(
                id = R.color.textTertiary))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            verticalSpacer(height = 8.dp)

            val sort = viewModel.sortBy.observeAsState().value

            DrawerItem(modifier = Modifier.clickable {
                viewModel.sortBy.value = NoteSortBy.UPDATED_DATE
                Prefs.noteSortBy = NoteSortBy.UPDATED_DATE
                RxBus.getDefault().send(SettingsEvent())
            }, isSelected = sort == NoteSortBy.UPDATED_DATE, text = "Updated Date", image = R.drawable.ic_sort)
            DrawerItem(modifier = Modifier.clickable {
                viewModel.sortBy.value = NoteSortBy.CREATED_DATE
                Prefs.noteSortBy = NoteSortBy.CREATED_DATE
                RxBus.getDefault().send(SettingsEvent())
            }, isSelected = sort == NoteSortBy.CREATED_DATE, text = "Created Date", image = R.drawable.ic_sort)
//            DrawerItem(modifier = Modifier.clickable {
//                viewModel.sortBy.value = NoteSortBy.TITLE
//                Prefs.noteSortBy = NoteSortBy.TITLE
//                RxBus.getDefault().send(SettingsEvent())
//            }, isSelected = sort == NoteSortBy.TITLE, text = LocaleConstants.TITLE.locale(), image = R.drawable.ic_sort)

        }
        verticalSpacer(height = 16)


        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val selectedFolder = viewModel.selectedFolder.observeAsState()

            val isSelected = selectedFolder.value == deletedFolderId
            DrawerItem(modifier = Modifier.clickable {
                if (!isSelected) {
                    viewModel.selectedFolder.value = deletedFolderId
                } else {
                    viewModel.selectedFolder.value = null
                }
                RxBus.getDefault().send(FolderEvent(viewModel.selectedFolder.value))
            }, isSelected = isSelected, text = "Deleted".locale(), image = R.drawable.ic_delete)
        }

        verticalSpacer(128)
    }
}

@Composable
fun DrawerItem(modifier:Modifier, isSelected:Boolean, text:String, image:Int?=null){
    Box(modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(if (isSelected) colorResource(id = R.color.colorPrimary).copy(
            0.2f) else colorResource(
            id = android.R.color.transparent))) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
            Image(modifier = Modifier
                .height(24.dp)
                .width(24.dp),
                painter = painterResource(
                    id = image ?: R.drawable.ic_folder),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (isSelected) colorResource(id = R.color.colorPrimary) else colorResource(
                        id = R.color.textTertiary)))
            horizontalSpacer(width = 12.dp)
            Text(text = text,
                fontFamily = lato,
                fontSize = 16.sp,
                maxLines = 1,
                color = if (isSelected) colorResource(id = R.color.colorPrimary) else colorResource(
                    id = R.color.textTertiary),
                fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun NotePersonalFragment.folders() {

    val folder = viewModel.selectedFolder.observeAsState()
    val deletedNotes = viewModel.deletedNotes.observeAsState().value ?: arrayListOf()

    val folders = requireActivity().getNotePropertyDao().getNoteProperties()
        .observeAsState().value?.filter { it.type == NotePropertyType.Folder }
        ?: arrayListOf()

    val isShowFolders = folders.isNotEmpty() || deletedNotes.isNotEmpty()
//    viewDataBinding?.folderContainer?.visibility = if (isShowFolders) View.VISIBLE else View.INVISIBLE

    if (isShowFolders) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.neutral_100_night_200_light))
        ) {
            LazyRow(modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Spacer(Modifier.width(8.dp))
                }
                itemsIndexed(
                    items = folders,
                    itemContent = { index, item ->
                        folder(modifier = Modifier.clickable {
                            if (viewModel.selectedFolder.value == item.content) {
                                viewModel.selectedFolder.value = null
                            } else {
                                viewModel.selectedFolder.value = item.content
                            }
                        }, name = item.content, isSelected = item.content == folder.value)
                    }
                )
                item {
                    if (deletedNotes.isNotEmpty()) {
                        deleted(modifier = Modifier.clickable {
                            if (viewModel.selectedFolder.value == deletedFolderId) {
                                viewModel.selectedFolder.value = null
                            } else {
                                viewModel.selectedFolder.value = deletedFolderId
                            }
                        }, isSelected = folder.value == deletedFolderId)
                    }
                    Spacer(Modifier.width(16.dp))
                }
            }
        }
    }
}

@Composable
fun NotePersonalFragment.folder(modifier: Modifier, name: String, isSelected: Boolean) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        backgroundColor = colorResource(
            id = if (isSelected) R.color.colorPrimary else R.color.card
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                painter = painterResource(id = R.drawable.ic_folder),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(id = if (isSelected) R.color.white else R.color.colorPrimary))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier,
                text = name,
                fontFamily = lato,
                fontSize = 14.sp,
                color = colorResource(
                    id = if (isSelected) R.color.white else R.color.textTertiary
                )
            )
        }
    }
}

@Composable
fun NotePersonalFragment.deleted(modifier: Modifier, isSelected: Boolean) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        backgroundColor = colorResource(
            id = if (isSelected) R.color.colorPrimary else R.color.neutral_0
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp),
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(id = if (isSelected) R.color.white else R.color.red))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier,
                text = LocaleConstants.DELETED.locale(),
                fontFamily = lato,
                fontSize = 14.sp,
                color = colorResource(
                    id = if (isSelected) R.color.white else R.color.textTertiary
                )
            )
        }
    }
}