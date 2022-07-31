//package com.wagyufari.dzikirqu.ui.v2.main.home.composable
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.DropdownMenu
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.constants.LocaleConstants
//import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
//import com.wagyufari.dzikirqu.model.PrayerTime
//import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsActivity
//import com.wagyufari.dzikirqu.ui.praytime.PraytimeActivity
//import com.wagyufari.dzikirqu.ui.search.SearchActivity
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getCurrentPrayerTimeString
//import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getTimeUntilNextPrayerString
//
//
//@Composable
//fun MainHomeHeader(praytime: PrayerTime, address: String) {
//
//    val context = LocalContext.current
//    var searchPopupEnabled by remember { mutableStateOf(false) }
//
//    Box(
//        Modifier
//            .fillMaxWidth()
//            .height(220.dp)
//    ) {
//        MainHomeHeaderBackground()
//        Column(Modifier.fillMaxSize()) {
//            Row(
//                modifier = Modifier
//                    .padding(12.dp)
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        "Sun, May 15, 2022",
//                        color = Color.White,
//                        fontFamily = lato,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text("14 Shawwal, 1443", color = Color.White, fontFamily = lato)
//                }
//                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    DropdownMenu(
//                        expanded = searchPopupEnabled,
//                        onDismissRequest = {
//                            searchPopupEnabled = false
//                        }) {
//                        Column(
//                            Modifier.padding(8.dp),
//                            verticalArrangement = Arrangement.spacedBy(16.dp)
//                        ) {
//                            Text(
//                                modifier = Modifier.clickable {
//                                    searchPopupEnabled = false
//                                    SearchActivity.startQuran(context)
//                                },
//                                text = LocaleConstants.SEARCH_QURAN.locale(), fontSize = 16.sp
//                            )
//                            Text(
//                                modifier = Modifier.clickable {
//                                    searchPopupEnabled = false
//                                    SearchActivity.startPrayer(context)
//                                },
//                                text = LocaleConstants.SEARCH_PRAYER.locale(), fontSize = 16.sp
//                            )
//                        }
//                    }
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_search),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .width(32.dp)
//                            .height(32.dp)
//                            .padding(2.dp)
//                            .clickable(enabled = true, onClick = {
//                                searchPopupEnabled = !searchPopupEnabled
//                            }),
//                        colorFilter = ColorFilter.tint(Color.White)
//                    )
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_settings),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .width(32.dp)
//                            .height(32.dp)
//                            .padding(2.dp)
//                            .clickable(enabled = true, onClick = {
//                                context.startActivity(SettingsActivity.newIntent(context))
//                            }),
//                        colorFilter = ColorFilter.tint(Color.White)
//                    )
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .clickable(indication = null,
//                        interactionSource = remember { MutableInteractionSource() }) {
//                        PraytimeActivity.start(context)
//                    }
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    LocaleConstants.NEXT_PRAYER.locale(),
//                    fontSize = 16.sp,
//                    color = Color.White,
//                    fontFamily = lato
//                )
//                Text(
//                    praytime.getCurrentPrayerTimeString() ?: "",
//                    fontSize = 32.sp,
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontFamily = lato
//                )
//                Text(
//                    praytime.getTimeUntilNextPrayerString(),
//                    fontSize = 16.sp,
//                    color = Color.White,
//                    fontFamily = lato
//                )
//                Row(modifier = Modifier.padding(2.dp)) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_location_primary),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .height(20.dp)
//                            .width(20.dp)
//                            .padding(2.dp),
//                        colorFilter = ColorFilter.tint(Color.White)
//                    )
//                    Text(address, color = Color.White, fontFamily = lato)
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun MainHomeHeaderBackground() {
//    Image(
//        painter = painterResource(id = R.drawable.radial),
//        contentDescription = "Radial",
//        modifier = Modifier.fillMaxSize(),
//        contentScale = ContentScale.Crop
//    )
//    Image(
//        painter = painterResource(id = R.drawable.ic_subtract_2),
//        contentDescription = "Decor",
//        modifier = Modifier
//            .alpha(0.4f)
//            .offset {
//                IntOffset(0, -80)
//            },
//    )
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter,
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.mosque),
//            contentDescription = "Mosque",
//            modifier = Modifier
//                .width(270.dp)
//                .height(120.dp)
//                .alpha(0.2f),
//            contentScale = ContentScale.FillBounds
//        )
//        Image(
//            painter = painterResource(id = R.drawable.ic_footer),
//            contentDescription = "Footer",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(32.dp),
//            contentScale = ContentScale.FillBounds
//        )
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(2.dp)
//                .background(colorResource(id = R.color.neutral_50)),
//        )
//    }
//}