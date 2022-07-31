//package com.wagyufari.dzikirqu.ui.v2.main.home.composable
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Card
//import androidx.compose.material.ExperimentalMaterialApi
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.constants.ReadModeConstants
//import com.wagyufari.dzikirqu.data.Prefs
//import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
//import com.wagyufari.dzikirqu.model.events.MainTabEvent
//import com.wagyufari.dzikirqu.model.events.MainTabType
//import com.wagyufari.dzikirqu.ui.read.ReadActivity
//import com.wagyufari.dzikirqu.ui.v2.main.home.findActivity
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//import com.wagyufari.dzikirqu.util.*
//
//@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
//@Composable
//fun MainHomeQuranLastRead(modifier: Modifier, surah: String, ayah: String) {
//    val context = LocalContext.current
//
//    Card(
//        modifier = modifier
//            .padding(horizontal = 16.dp),
//        shape = RoundedCornerShape(16.dp),
//        onClick = {
//            val lastRead = Prefs.quranLastRead
//            when (Prefs.defaultQuranReadMode) {
//                ReadModeConstants.VERTICAL -> {
//                    ReadActivity.startSurah(context, lastRead.surah, lastRead.ayah)
//                    RxBus
//                        .getDefault()
//                        .send(MainTabEvent(MainTabType.QURAN))
//                }
//                ReadModeConstants.PAGED -> {
//                    context.io {
//                    }
//                }
//                ReadModeConstants.ASK_AGAIN -> {
//                    val ayahLineDao = PersistenceDatabase
//                        .getDatabase(context)
//                        .ayahLineDao()
//                    context.main {
//                        if (SplitInstallManagerFactory.create(context).installedModules
//                                .contains(
//                                    "pagedquran"
//                                )
//                                .not() || ayahLineDao.getCount() != 6236
//                        ) {
//                            ReadActivity.startSurah(context, lastRead.surah, lastRead.ayah)
//                            RxBus
//                                .getDefault()
//                                .send(MainTabEvent(MainTabType.QURAN))
//                        } else {
//                            context.findActivity()?.showQuranModeDialog(lastRead.surah, lastRead.ayah)
//                        }
//                    }
//                }
//            }
//        }
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Box(modifier = Modifier.fillMaxSize())
//            Image(
//                modifier = Modifier,
//                painter = painterResource(id = R.drawable.radial),
//                contentDescription = "Background",
//                contentScale = ContentScale.FillBounds
//            )
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box {
//                    Text("Terakhir dibaca", fontFamily = lato, color = Color.White)
//                    Box(
//                        contentAlignment = Alignment.BottomStart,
//                        modifier = Modifier.fillMaxHeight()
//                    ) {
//                        Column {
//                            Text(
//                                surah,
//                                fontFamily = lato,
//                                color = Color.White,
//                                fontSize = 24.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(ayah, fontFamily = lato, color = Color.White)
//                        }
//                    }
//                }
//                Image(
//                    modifier = Modifier
//                        .width(80.dp)
//                        .height(80.dp)
//                        .alpha(0.3f),
//                    painter = painterResource(id = R.drawable.ic_quran_calligraphy),
//                    contentDescription = null
//                )
//            }
//        }
//    }
//}