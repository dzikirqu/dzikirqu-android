//package com.wagyufari.dzikirqu.ui.v2.main.home
//
//import android.content.Context
//import android.content.ContextWrapper
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.wagyufari.dzikirqu.ui.v2.main.home.composable.MainHomeFeatureButtons
//import com.wagyufari.dzikirqu.ui.v2.main.home.composable.MainHomeHeader
//import com.wagyufari.dzikirqu.ui.v2.main.home.composable.MainHomeQuranLastRead
//import com.wagyufari.dzikirqu.ui.v2.main.home.composable.MutiaraIman
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//
//@Composable
//fun MainHomeScreen(viewModel: MainHomeV2ViewModel, navController: NavHostController) {
//
//    viewModel.buildPrayerTime()
//
//    val praytime by remember { viewModel.praytime }
//    val address by remember { viewModel.address }
//    val lastReadSurah by remember { viewModel.lastReadSurah }
//    val lastReadAyah by remember { viewModel.lastReadAyah }
//
//    LaunchedEffect(null) {
//        viewModel.setUpLastRead()
//    }
//
//    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        MainHomeHeader(praytime, address)
//        Spacer(modifier = Modifier.height(20.dp))
//        Text(
//            "Assalamu'alaikum",
//            fontFamily = lato,
//            fontWeight = FontWeight.Bold,
//            fontSize = 24.sp
//        )
//        Text("Udah baca Qur'an hari ini?", fontFamily = lato, fontSize = 16.sp)
//        MainHomeFeatureButtons(Modifier, navController = navController)
//        MainHomeQuranLastRead(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(128.dp),
//            lastReadSurah,
//            lastReadAyah
//        )
//        MutiaraIman(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        )
//    }
//}
//
//
//
//
//
//fun Context.findActivity(): AppCompatActivity? = when (this) {
//    is AppCompatActivity -> this
//    is ContextWrapper -> baseContext.findActivity()
//    else -> null
//}