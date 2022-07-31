//package com.wagyufari.dzikirqu.ui.v2.main.quran.composable.items
//
//import android.content.Context
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Divider
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.SeparatorItem
//import com.wagyufari.dzikirqu.constants.ReadModeConstants
//import com.wagyufari.dzikirqu.data.Prefs
//import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
//import com.wagyufari.dzikirqu.model.Surah
//import com.wagyufari.dzikirqu.model.events.MainTabEvent
//import com.wagyufari.dzikirqu.model.events.MainTabType
//import com.wagyufari.dzikirqu.ui.read.ReadActivity
//import com.wagyufari.dzikirqu.ui.v2.main.home.findActivity
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//import com.wagyufari.dzikirqu.ui.v2.theme.surah
//import com.wagyufari.dzikirqu.util.*
//import com.wagyufari.dzikirqu.util.StringExt.getText
//
//@Composable
//fun ItemSurah(surahData: Surah, index:Int) {
//    val context = LocalContext.current
//    Column(
//        Modifier
//            .fillMaxWidth()
//            .clickable {
//                when (Prefs.defaultQuranReadMode) {
//                    ReadModeConstants.VERTICAL -> {
//                        ReadActivity.startSurah(context, surahData.id)
//                        RxBus
//                            .getDefault()
//                            .send(MainTabEvent(MainTabType.QURAN))
//                    }
//                    ReadModeConstants.PAGED -> {
//                        context.io {
//                        }
//                    }
//                    ReadModeConstants.ASK_AGAIN -> {
//                        val ayahLineDao = PersistenceDatabase
//                            .getDatabase(context)
//                            .ayahLineDao()
//                        context.main {
//                            if (SplitInstallManagerFactory.create(context).installedModules
//                                    .contains(
//                                        "pagedquran"
//                                    )
//                                    .not() || ayahLineDao.getCount() != 6236
//                            ) {
//                                ReadActivity.startSurah(context, surahData.id)
//                                RxBus
//                                    .getDefault()
//                                    .send(MainTabEvent(MainTabType.QURAN))
//                            } else {
//                                context.findActivity()?.showQuranModeDialog(surahData.id)
//                            }
//                        }
//                    }
//                }
//            }
//    ) {
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(index.toString(), fontFamily = lato, color = colorResource(id = R.color.textPrimary))
//                Spacer(Modifier.width(24.dp))
//                Column {
//                    Text(
//                        surahData.name,
//                        fontSize = 16.sp,
//                        fontFamily = lato,
//                        color = colorResource(id = R.color.textPrimary)
//                    )
//                    Text(
//                        surahData.translation.getText(),
//                        fontFamily = lato,
//                        color = colorResource(id = R.color.textTertiary)
//                    )
//                }
//            }
//            Text(
//                getArabicCalligraphy(context, surahData.id),
//                fontSize = 24.sp,
//                fontFamily = surah,
//                color = colorResource(id = R.color.textPrimary)
//            )
//        }
//        Divider()
//    }
//}
//
//private fun getArabicCalligraphy(mContext: Context, surah: Int): String {
//    return Gson().fromJson<ArrayList<SeparatorItem>>(
//        FileUtils.getJsonStringFromAssets(
//            mContext,
//            "json/quran/paged/separator.json"
//        ), object :
//            TypeToken<ArrayList<SeparatorItem>>() {}.type
//    ).filter { it.surah == surah }.firstOrNull()?.unicode ?: ""
//}