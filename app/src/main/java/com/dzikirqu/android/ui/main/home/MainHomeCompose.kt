package com.dzikirqu.android.ui.main.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.dzikirqu.android.R
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.*
import com.dzikirqu.android.model.DailyReminder.Companion.getEndTimeDate
import com.dzikirqu.android.model.DailyReminder.Companion.getEndTimeUIString
import com.dzikirqu.android.model.DailyReminder.Companion.getStartTimeDate
import com.dzikirqu.android.model.DailyReminder.Companion.getStartTimeUIString
import com.dzikirqu.android.model.DailyReminderParent
import com.dzikirqu.android.model.Prayer
import com.dzikirqu.android.model.events.MainTabEvent
import com.dzikirqu.android.model.events.MainTabType
import com.dzikirqu.android.ui.bsd.StartReadQuranBSD
import com.dzikirqu.android.ui.bsd.hadithbook.HadithBookBSD
import com.dzikirqu.android.ui.dailyreminder.DailyReminderActivity
import com.dzikirqu.android.ui.main.note.NoteActivity
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.*
import com.dzikirqu.android.util.StringExt.getText

@Composable
fun MainHomeFragment.Compose() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Header()
        verticalSpacer(16)
        Text("Assalamu'alaikum",
            fontFamily = lato,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(
                id = R.color.textPrimary))
        Text(LocaleConstants.HAVE_YOU_READ_THE_QURAN_TODAY_Q.locale(),
            fontFamily = lato,
            fontSize = 16.sp,
            color = colorResource(
                id = R.color.textTertiary))
        verticalSpacer(16)
        Column(Modifier.padding(horizontal = 10.dp)) {
            Feature()
            verticalSpacer(height = 16.dp)
            QuranLastRead()
            verticalSpacer(height = 16.dp)
            Note()
            verticalSpacer(height = 16.dp)
        }
        Highlights()
        DailyReminder()

    }
}

@Composable
private fun MainHomeFragment.Header() {
    ConstraintLayout(modifier = Modifier.background(brush = Brush.radialGradient(listOf(
        colorResource(id = R.color.colorPrimary),
        colorResource(id = R.color.colorPrimaryDark))))) {

        val (masjid, circle) = createRefs()

        Image(modifier = Modifier
            .alpha(0.4f)
            .fillMaxWidth()
            .fillMaxHeight(),
            painter = painterResource(id = R.drawable.ic_subtract_2),
            contentDescription = null,
            contentScale = ContentScale.Fit)

        Image(modifier = Modifier
            .constrainAs(masjid) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .alpha(0.2f)
            .width(270.dp)
            .height(120.dp),
            painter = painterResource(id = R.drawable.mosque),
            contentDescription = null)

        with(LocalDensity.current) {
            Column(Modifier
                .fillMaxWidth()
                .padding(top = Prefs.statusBarHeight.toDp()),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(text = viewModel.dataManager.getGregorianDate(),
                            fontFamily = lato,
                            fontSize = 12.sp,
                            color = colorResource(
                                id = R.color.white),
                            fontWeight = FontWeight.Bold)
                        Text(text = viewModel.dataManager.getHijriDate(),
                            fontFamily = lato,
                            fontSize = 12.sp,
                            color = colorResource(
                                id = R.color.white))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Image(modifier = Modifier
                            .height(28.dp)
                            .width(28.dp)
                            .nonRippleClickable {
                                onClickBookmark()
                            },
                            painter = painterResource(id = R.drawable.ic_bookmarks),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                colorResource(id = R.color.white)))
                        AndroidView(factory = {
                            ComposeView(it).apply {
                                setContent {
                                    Image(modifier = Modifier
                                        .height(28.dp)
                                        .width(28.dp)
                                        .width(28.dp)
                                        .nonRippleClickable {
                                            onClickSearch(this)
                                        },
                                        painter = painterResource(id = R.drawable.ic_search),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(
                                            colorResource(id = R.color.white)))
                                }
                            }
                        })
                        Image(modifier = Modifier
                            .height(28.dp)
                            .width(28.dp)
                            .width(28.dp)
                            .nonRippleClickable {
                                onClickSettings()
                            },
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                colorResource(id = R.color.white)))
                    }
                }

                val textUntil = viewModel.textUntil.observeAsState().value.toString()
                val textPrayerTime = viewModel.textPrayerTime.observeAsState().value.toString()
                val address = viewModel.address.observeAsState().value.toString()
                val isPrayerTimeHidden = viewModel.isPrayerTimeHidden.observeAsState().value
                val isPrayerTimeLoading = viewModel.isPrayerTimeLoading.observeAsState().value
                val isPrayerTimeNeedsPermission =
                    viewModel.isPrayerTimeNeedsPermission.observeAsState().value

                if (isPrayerTimeLoading == true) {
                    CircularProgressIndicator(modifier = Modifier
                        .height(32.dp)
                        .width(32.dp),
                        color = colorResource(id = R.color.white))
                }

                if (isPrayerTimeNeedsPermission == true) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 48.dp,
                            top = 12.dp,
                            start = 24.dp,
                            end = 24.dp)) {
                        Text(text = LocaleConstants.THE_APP_NEEDS_LOCATION_PERMISSION_TO_GET_ACCURATE_PRAYER_TIME.locale(),
                            fontFamily = lato,
                            fontSize = 16.sp,
                            color = colorResource(
                                id = R.color.white), textAlign = TextAlign.Center)
                        Button(onClick = {
                            onClickGrantPermission()
                        },
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
                            shape = RoundedCornerShape(4.dp)) {
                            Text(text = LocaleConstants.GRANT_PERMISSION.locale().uppercase(),
                                fontFamily = lato,
                                fontSize = 14.sp,
                                color = colorResource(
                                    id = R.color.colorPrimary))
                        }
                    }
                }

                if (isPrayerTimeHidden == false) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(bottom = 48.dp, top = 4.dp)
                            .nonRippleClickable {
                                onClickPraytime()
                            }) {
                        Text(text = LocaleConstants.NEXT_PRAYER.locale(),
                            fontFamily = lato,
                            fontSize = 16.sp,
                            color = colorResource(
                                id = R.color.white))
                        Text(text = textPrayerTime,
                            fontFamily = lato,
                            fontSize = 32.sp,
                            color = colorResource(
                                id = R.color.white),
                            fontWeight = FontWeight.Bold)
                        Text(text = textUntil,
                            fontFamily = lato,
                            fontSize = 16.sp,
                            color = colorResource(
                                id = R.color.white))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                                .padding(4.dp),
                                painter = painterResource(id = R.drawable.ic_location),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    colorResource(id = R.color.white)))
                            Text(text = address,
                                fontFamily = lato,
                                fontSize = 14.sp,
                                color = colorResource(
                                    id = R.color.white))
                        }
                    }
                }


            }

        }

        Box(modifier = Modifier
            .constrainAs(circle) {
                bottom.linkTo(parent.bottom)
            }
            .fillMaxWidth()
            .height(32.dp)) {
            Image(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
                painter = painterResource(id = R.drawable.ic_footer),
                contentDescription = null,
                contentScale = ContentScale.FillBounds)
            Box(modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(2.dp)
                .background(colorResource(id = R.color.neutral_100)))
        }
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun MainHomeFragment.Note() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp,
        backgroundColor = colorResource(id = R.color.card)) {
        Box(Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
                startActivity(Intent(requireActivity(), NoteActivity::class.java))
            },
            indication = rememberRipple(bounded = true)
        )) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(shape = RoundedCornerShape(8.dp),
                        elevation = 1.dp,
                        backgroundColor = colorResource(
                            id = R.color.neutral_100)) {
                        Box(Modifier.padding(12.dp)) {
                            Image(modifier = Modifier
                                .height(24.dp)
                                .width(24.dp),
                                painter = painterResource(id = R.drawable.ic_note),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(colorResource(
                                    id = R.color.colorPrimary)))
                        }
                    }
                    horizontalSpacer(width = 16.dp)
                    Column {
                        val notesCount =
                            requireActivity().getNoteDao().getNotes()
                                .observeAsState().value?.count()
                        Text(modifier = Modifier, text = LocaleConstants.NOTES.locale(),
                            fontFamily = lato,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(
                                id = R.color.textPrimary))
                        Text(modifier = Modifier,
                            text = String.format(LocaleConstants.N_NOTES.locale(),
                                notesCount.toString()),
                            fontFamily = lato,
                            fontSize = 12.sp,
                            color = colorResource(
                                id = R.color.textTertiary))
                    }
                }
                Firebase.auth.currentUser?.let {
                    Image(
                        modifier = Modifier
                            .height(28.dp)
                            .width(28.dp)
                            .clip(CircleShape),
                        painter = rememberAsyncImagePainter(model = it.photoUrl),
                        contentDescription = null,
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainHomeFragment.DailyReminder() {
    Row(Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier, text = LocaleConstants.DAILY_PRAYERS.locale(),
            fontFamily = lato,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(
                id = R.color.textPrimary))
        Text(modifier = Modifier.clickable {
            DailyReminderActivity.start(requireActivity())
        }, text = LocaleConstants.VIEW_ALL.locale(),
            fontFamily = lato,
            fontSize = 14.sp,
            color = colorResource(
                id = R.color.textTertiary))
    }

    Column(Modifier
        .fillMaxWidth()) {
        viewModel.time.observeAsState().value?.let { time ->
            val dailyReminderParent =
                requireActivity().getDailyReminderParentDao().getDailyReminder()
                    .observeAsState().value?.filter { time >= it.getStartTimeDate() && time <= it.getEndTimeDate() }
                    ?.toHashSet()?.toCollection(
                        arrayListOf())
                    ?: listOf()
            if (dailyReminderParent.filter { time <= it.getEndTimeDate() && time >= it.getStartTimeDate() }
                    .isEmpty()) {
                verticalSpacer(height = 16.dp)
                Text(modifier = Modifier.fillMaxWidth(),
                    text = LocaleConstants.THERE_ARE_NO_PRAYER_AT_THIS_POINT_OF_TIME.locale(),
                    textAlign = TextAlign.Center,
                    fontFamily = lato,
                    fontSize = 16.sp,
                    color = colorResource(
                        id = R.color.textTertiary))
                return@let
            }

            val cardWidth = LocalConfiguration.current.screenWidthDp * 0.76

            verticalSpacer(12)

            if (dailyReminderParent.count() > 1) {
                CompositionLocalProvider(
                    LocalOverScrollConfiguration.provides(null)
                ) {
                    LazyRow(Modifier, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        item { horizontalSpacer(width = 0.dp) }
                        items(dailyReminderParent) {
                            DailyReminderCard(requireActivity(),
                                modifier = Modifier.width(cardWidth.dp),
                                parent = it, true)
                        }
                        item { horizontalSpacer(width = 16.dp) }
                    }
                }
            } else if (dailyReminderParent.isNotEmpty()) {
                DailyReminderCard(requireActivity(), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp), parent = dailyReminderParent.first(), true)
            }
        }
    }
}

@Composable
fun DailyReminderCard(
    context: Context,
    modifier: Modifier,
    parent: DailyReminderParent,
    isExpanded: Boolean? = false,
) {

    var isExpanded by remember { mutableStateOf(isExpanded ?: false) }

    Card(
        modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        backgroundColor = colorResource(id = R.color.card),
    ) {
        Box {
            Column(Modifier
                .padding(16.dp)
                .animateContentSize(),
                horizontalAlignment = Alignment.Start) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = null) {
                        isExpanded = !isExpanded
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(parent.title.toString(),
                            fontFamily = lato,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(
                                id = R.color.textSecondary))
                        verticalSpacer(height = 4.dp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(modifier = Modifier
                                .height(16.dp)
                                .width(16.dp),
                                painter = painterResource(id = R.drawable.ic_access_time_filled),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(colorResource(id = R.color.colorPrimary)))
                            horizontalSpacer(width = 4.dp)
                            Text("${parent.getStartTimeUIString()} - ${parent.getEndTimeUIString()}",
                                fontFamily = lato,
                                fontSize = 12.sp,
                                color = colorResource(
                                    id = R.color.textTertiary))
                        }
                    }
                    Image(modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_chevron_down),
                        contentDescription = null)
                }

                if (isExpanded) {
                    verticalSpacer(height = 8)
                    Column(modifier = Modifier
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        context.getDailyReminderDao()
                            .getDailyReminderByParentId(parent.id ?: -1)
                            .observeAsState().value?.forEach {
                                val prayer = context.getPrayerDao()
                                    .getPrayerById(it.typeId.toString())
                                    .observeAsState()
                                prayer.value?.let { it1 ->
                                    ItemDailyReminder(prayer = it1,
                                        context)
                                }
                            }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemDailyReminder(prayer: Prayer, context: Context) {
    Card(modifier = Modifier.fillMaxWidth(),
        backgroundColor = colorResource(id = R.color.card),
        onClick = {
            ReadActivity.startPrayer(context, prayer.id.toInt())
        },
        elevation = 0.dp,
        border = BorderStroke(1.dp, colorResource(id = R.color.divider)),
        shape = RoundedCornerShape(4.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            text = prayer.title?.getText().toString(),
            fontFamily = lato,
            fontSize = 14.sp,
            color = colorResource(
                id = R.color.textSecondary)
        )
    }
}

@Composable
private fun MainHomeFragment.Highlights() {
    val bookmarks =
        requireActivity().getBookmarkDao().getHighlights().observeAsState().value
            ?: listOf()
    var isExpanded by remember { mutableStateOf(false) }

    if (bookmarks.isEmpty()) return
    Column {
        Column(Modifier
            .fillMaxWidth()
            .animateContentSize()
            .background(colorResource(id = R.color.neutral_200))) {
            Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier
                    .weight(1f),
                    text = LocaleConstants.QUICK_ACCESS.locale(),
                    fontFamily = lato,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.textTertiary))
                Image(modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .clickable {
                        isExpanded = !isExpanded
                    },
                    painter = painterResource(id = if (isExpanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.textTertiary)))
            }

            AnimatedVisibility(visible = isExpanded) {
                FlowRow(Modifier.padding(12.dp)) {
                    bookmarks.forEach {
                        PrayerExt.highlighted(bookmark = it)
                    }
                }
                verticalSpacer(height = 8)
            }

            AnimatedVisibility(visible = !isExpanded) {
                LazyRow(Modifier.padding(vertical = 12.dp)) {
                    item {
                        horizontalSpacer(width = 12)
                        bookmarks.forEach {
                            PrayerExt.highlighted(bookmark = it)
                        }
                        horizontalSpacer(width = 8)
                    }
                }
            }
        }
    }
    verticalSpacer(height = 16.dp)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainHomeFragment.Instagram() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp), shape = RoundedCornerShape(8.dp), onClick = {
        val uri: Uri = Uri.parse("https://www.instagram.com/mutiaraimanid/")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                uri
            )
        )
    }, backgroundColor = colorResource(id = R.color.card)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(modifier = Modifier
                    .clip(CircleShape)
                    .width(32.dp)
                    .height(32.dp),
                    painter = painterResource(id = R.drawable.mutiaraimanpp),
                    contentDescription = null)
                horizontalSpacer(width = 8)
                Column {
                    Text(text = "Mutiara Iman",
                        fontFamily = lato,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(
                            id = R.color.textPrimary))
                    Text(text = "mutiaraimanid",
                        fontFamily = lato,
                        color = colorResource(id = R.color.textTertiary), fontSize = 12.sp)
                }
            }
            Image(modifier = Modifier
                .height(24.dp)
                .width(24.dp),
                painter = painterResource(id = R.drawable.ic_instagram),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorResource(id = R.color.neutral_400)))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainHomeFragment.QuranLastRead() {

    val surah = viewModel.lastReadSurah.observeAsState()
    val ayah = viewModel.lastReadAyah.observeAsState()

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier
            .background(brush = Brush.radialGradient(listOf(colorResource(id = R.color.colorPrimary),
                colorResource(id = R.color.colorPrimaryDark))))
            .fillMaxWidth()
            .combinedClickable(onClick = {
                StartReadQuranBSD
                    .newInstantInstance(surah = Prefs.quranLastRead.surah,
                        ayah = Prefs.quranLastRead.ayah)
                    .show(childFragmentManager, "")
            }, onLongClick = {
                StartReadQuranBSD
                    .newSelectionInstance(surah = Prefs.quranLastRead.surah,
                        ayah = Prefs.quranLastRead.ayah)
                    .show(childFragmentManager, "")
            })
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier) {
                Text(LocaleConstants.LAST_READ.locale(), fontFamily = lato, fontSize = 14.sp, color = colorResource(
                    id = R.color.white))
                verticalSpacer(height = 16.dp)
                Text(text = surah.value ?: "",
                    fontFamily = lato,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        id = R.color.white))
                Text(text = ayah.value ?: "",
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = R.color.white))
            }
            Image(modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .padding(0.dp),
                painter = painterResource(id = R.drawable.ic_quran_calligraphy),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(id = R.color.black).copy(alpha = 0.4f)))
        }
    }

//    Card(modifier = Modifier
//        .fillMaxWidth()
//        .padding(horizontal = 12.dp)
//        .height(124.dp),
//        shape = RoundedCornerShape(16.dp),
//        backgroundColor = colorResource(id = R.color.colorPrimary)) {
//        Image(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .combinedClickable(onClick = {
//                    StartReadQuranBSD
//                        .newInstantInstance(surah = Prefs.quranLastRead.surah,
//                            ayah = Prefs.quranLastRead.ayah)
//                        .show(childFragmentManager, "")
//                }, onLongClick = {
//                    StartReadQuranBSD
//                        .newSelectionInstance(surah = Prefs.quranLastRead.surah,
//                            ayah = Prefs.quranLastRead.ayah)
//                        .show(childFragmentManager, "")
//                }),
//            painter = painterResource(id = R.drawable.radial),
//            contentScale = ContentScale.FillBounds,
//            contentDescription = null,
//        )
//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically) {
//            Column(Modifier) {
//                Text(LocaleConstants.LAST_READ.locale(),
//                    fontFamily = lato,
//                    fontSize = 14.sp,
//                    color = colorResource(
//                        id = R.color.white))
//                verticalSpacer(height = 16.dp)
//                Text(text = surah.value ?: "",
//                    fontFamily = lato,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = colorResource(
//                        id = R.color.white))
//                Text(text = ayah.value ?: "",
//                    fontFamily = lato,
//                    fontSize = 14.sp,
//                    color = colorResource(
//                        id = R.color.white))
//            }
//            Image(modifier = Modifier
//                .height(80.dp)
//                .width(80.dp)
//                .padding(0.dp),
//                painter = painterResource(id = R.drawable.ic_quran_calligraphy),
//                contentDescription = null,
//                colorFilter = ColorFilter.tint(colorResource(id = R.color.black).copy(alpha = 0.4f)))
//        }
//    }
}

@Composable
private fun MainHomeFragment.Feature() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FeatureCard(
            modifier = Modifier
                .weight(1f),
            LocaleConstants.QURAN.locale(),
            R.drawable.ic_quran
        ) {
            RxBus
                .getDefault()
                .send(MainTabEvent(MainTabType.QURAN))
        }
        FeatureCard(
            modifier = Modifier
                .weight(1f),
            LocaleConstants.PRAYER.locale(),
            R.drawable.ic_book
        ) {
            RxBus
                .getDefault()
                .send(MainTabEvent(MainTabType.PRAYER))
        }
        FeatureCard(
            modifier = Modifier
                .weight(1f),
            LocaleConstants.HADITH.locale(),
            R.drawable.ic_collection_book
        ) {
            HadithBookBSD
                .newInstance()
                .show(childFragmentManager, "")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FeatureCard(modifier: Modifier, name: String, image: Int, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        elevation = 1.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = colorResource(id = R.color.card),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onClick()
                    },
                    indication = rememberRipple(bounded = true)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            verticalSpacer(height = 8)
            Image(
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
                    .padding(0.dp),
                painter = painterResource(id = image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(id = R.color.colorPrimary))
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = name,
                fontFamily = lato,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                color = colorResource(id = R.color.textPrimary),
                textAlign = TextAlign.Center
            )
            verticalSpacer(height = 8)
        }
    }
}


//fun MainHomeFragment.Header() {
//    ConstraintLayout(modifier = Modifier
//        .fillMaxWidth()
//        .background(colorResource(id = R.color.green))
//        .height(240.dp)) {
//        val (background, footer, footerFiller, mosque, decor, prayerTextContainer, permission, progress, topContent) = createRefs()
//        val isPrayerTimeHidden = viewModel.isPrayerTimeHidden.observeAsState().value ?: true
//        val isPrayerTimeNeedsPermission =
//            viewModel.isPrayerTimeNeedsPermission.observeAsState().value ?: true
//        val isPrayertimeLoading = viewModel.isPrayerTimeLoading.observeAsState().value ?: true
//        val statusBarHeight = viewModel.statusBarHeight.observeAsState().value ?: 0
//        val textPrayerTime = viewModel.textPrayerTime.observeAsState().value ?: "Loading..."
//        val textUntil = viewModel.textUntil.observeAsState().value ?: "Loading..."
//        val address = viewModel.address.observeAsState().value ?: "Loading..."
//
//        HeaderBackground(background = background,
//            decor = decor,
//            mosque = mosque,
//            footer = footer,
//            footerFiller = footerFiller)
//
//        with(LocalDensity.current) {
//
//            Row(Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .constrainAs(topContent) {
//                    top.linkTo(parent.top, margin = statusBarHeight.toDp())
//                }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
//                Column {
//                    Text(text = viewModel.dataManager.getGregorianDate(),
//                        fontFamily = lato,
//                        fontSize = 12.sp,
//                        color = colorResource(
//                            id = R.color.white))
//                    Text(text = viewModel.dataManager.getHijriDate(),
//                        fontFamily = lato,
//                        fontSize = 12.sp,
//                        color = colorResource(
//                            id = R.color.white))
//                }
//                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    Image(modifier = Modifier
//                        .height(32.dp)
//                        .width(32.dp)
//                        .padding(4.dp)
//                        .clickable {
//                            BookmarkBSD().show(childFragmentManager, "")
//                        },
//                        painter = painterResource(id = R.drawable.ic_bookmarks),
//                        contentDescription = null,
//                        colorFilter = ColorFilter.tint(
//                            colorResource(id = R.color.white)))
////                    Image(modifier = Modifier
////                        .height(32.dp)
////                        .width(32.dp)
////                        .padding(2.dp).clickable{
////
////                        },
////                        painter = painterResource(id = R.drawable.ic_search),
////                        contentDescription = null,
////                        colorFilter = ColorFilter.tint(
////                            colorResource(id = R.color.white)))
//                    Image(modifier = Modifier
//                        .height(32.dp)
//                        .width(32.dp)
//                        .padding(2.dp)
//                        .clickable {
//                            SettingsActivity.start(requireActivity())
//                        },
//                        painter = painterResource(id = R.drawable.ic_settings),
//                        contentDescription = null,
//                        colorFilter = ColorFilter.tint(
//                            colorResource(id = R.color.white)))
//                }
//            }
//
//            if (isPrayerTimeNeedsPermission) {
//                Column(Modifier
//                    .fillMaxWidth()
//                    .constrainAs(permission) {
//                        top.linkTo(parent.top, margin = 64.dp + statusBarHeight.toDp())
//                    }, horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(modifier = Modifier.padding(horizontal = 16.dp),
//                        text = LocaleConstants.THE_APP_NEEDS_LOCATION_PERMISSION_TO_GET_ACCURATE_PRAYER_TIME.locale(),
//                        fontFamily = lato,
//                        fontSize = 14.sp,
//                        color = colorResource(
//                            id = R.color.white),
//                        textAlign = TextAlign.Center)
//                    verticalSpacer(8.dp)
//                    Button(onClick = { onClickGrantPermission() },
//                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(
//                            id = R.color.white))) {
//                        Text(text = LocaleConstants.GRANT_PERMISSION.locale(),
//                            fontFamily = lato,
//                            fontSize = 14.sp,
//                            color = colorResource(
//                                id = R.color.colorPrimary))
//                    }
//                }
//            }
//            if (isPrayertimeLoading) {
//                CircularProgressIndicator(Modifier
//                    .height(32.dp)
//                    .width(32.dp)
//                    .constrainAs(progress) {
//                        top.linkTo(parent.top, margin = 64.dp + statusBarHeight.toDp())
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }, color = colorResource(id = R.color.white))
//            }
//            if (!isPrayerTimeHidden) {
//                Column(modifier = Modifier
//                    .fillMaxWidth()
//                    .constrainAs(prayerTextContainer) {
//                        top.linkTo(parent.top, margin = 64.dp + statusBarHeight.toDp())
//                    }, horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(text = LocaleConstants.NEXT_PRAYER.locale(),
//                        fontFamily = lato,
//                        fontSize = 16.sp,
//                        color = colorResource(
//                            id = R.color.white))
//                    Text(text = textPrayerTime,
//                        fontFamily = lato,
//                        fontSize = 32.sp,
//                        color = colorResource(
//                            id = R.color.white), fontWeight = FontWeight.Bold)
//                    Text(text = textUntil,
//                        fontFamily = lato,
//                        fontSize = 16.sp,
//                        color = colorResource(
//                            id = R.color.white))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Image(
//                            modifier = Modifier
//                                .width(16.dp)
//                                .height(16.dp),
//                            painter = painterResource(id = R.drawable.ic_location),
//                            contentDescription = null,
//                            colorFilter = ColorFilter.tint(colorResource(id = R.color.white))
//                        )
//                        Text(text = address,
//                            fontFamily = lato,
//                            fontSize = 14.sp,
//                            color = colorResource(
//                                id = R.color.white))
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
private fun ConstraintLayoutScope.HeaderBackground(
    background: ConstrainedLayoutReference,
    decor: ConstrainedLayoutReference,
    mosque: ConstrainedLayoutReference,
    footer: ConstrainedLayoutReference,
    footerFiller: ConstrainedLayoutReference,
) {
    Image(modifier = Modifier.constrainAs(background) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
    }, painter = painterResource(id = R.drawable.radial),
        contentDescription = null,
        contentScale = ContentScale.Crop)
    Image(modifier = Modifier
        .offset(y = (-40).dp)
        .constrainAs(decor) {
            linkTo(parent.start, parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.percent(1.5f)
            bottom.linkTo(parent.bottom)
        }, painter = painterResource(id = R.drawable.ic_subtract_2), contentDescription = null)
    Image(modifier = Modifier
        .width(270.dp)
        .height(120.dp)
        .alpha(0.2f)
        .constrainAs(mosque) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        },
        painter = painterResource(id = R.drawable.mosque),
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            colorResource(id = R.color.black)),
        contentScale = ContentScale.FillBounds)
    Image(modifier = Modifier
        .fillMaxWidth()
        .height(32.dp)
        .constrainAs(footer) {
            bottom.linkTo(parent.bottom)
        },
        painter = painterResource(id = R.drawable.ic_footer),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        colorFilter = ColorFilter.tint(colorResource(id = R.color.neutral_50)))
    Box(Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(colorResource(id = R.color.neutral_50))
        .constrainAs(footerFiller) {
            bottom.linkTo(parent.bottom)
        })
}