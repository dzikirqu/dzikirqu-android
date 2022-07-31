//package com.wagyufari.dzikirqu.ui.v2.main.home.composable
//
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Card
//import androidx.compose.material.ExperimentalMaterialApi
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.ui.bsd.hadithbook.HadithBookBSD
//import com.wagyufari.dzikirqu.ui.bsd.prayerbook.PrayerBookBSD
//import com.wagyufari.dzikirqu.ui.v2.main.BottomNavItem
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//
//
//
//@Composable
//fun MainHomeFeatureButtons(modifier: Modifier, navController: NavHostController) {
//    val context = (LocalContext.current as AppCompatActivity)
//    Row(
//        modifier = modifier.padding(16.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        MainHomeFeatureButton(
//            modifier = Modifier.weight(1f),
//            "Al-Qu'ran",
//            painterResource(id = R.drawable.ic_quran)
//        ) {
//            navController.navigate(BottomNavItem.Quran.screen_route) {
//                navController.graph.startDestinationRoute?.let { screen_route ->
//                    popUpTo(screen_route) {
//                        saveState = true
//                    }
//                }
//                launchSingleTop = true
//                restoreState = true
//            }
//        }
//        MainHomeFeatureButton(
//            modifier = Modifier.weight(1f),
//            "Dzikir & Do'a",
//            painterResource(id = R.drawable.ic_prayer)
//        ) {
//            PrayerBookBSD.newInstance().show(context.supportFragmentManager, "")
//        }
//        MainHomeFeatureButton(
//            modifier = Modifier.weight(1f),
//            "Hadist",
//            painterResource(id = R.drawable.ic_hadith)
//        ) {
//            HadithBookBSD.newInstance().show(context.supportFragmentManager, "")
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun MainHomeFeatureButton(
//    modifier: Modifier = Modifier,
//    title: String,
//    image: Painter,
//    onClick: () -> Unit = {}
//) {
//    Card(modifier = modifier, shape = RoundedCornerShape(8.dp), onClick = {
//        onClick()
//    }) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.padding(6.dp)
//        ) {
//            Image(
//                painter = image, contentDescription = "", modifier = Modifier
//                    .height(38.dp)
//                    .width(38.dp)
//            )
//            Text(text = title, fontSize = 12.sp, fontFamily = lato, fontWeight = FontWeight.Bold)
//        }
//    }
//}