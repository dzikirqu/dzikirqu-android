//package com.wagyufari.dzikirqu.ui.v2.main.quran.composable.header
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Card
//import androidx.compose.material.LinearProgressIndicator
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.ui.v2.main.quran.composable.ItemMainQuranLastRead
//import com.wagyufari.dzikirqu.ui.v2.main.quran.composable.MainQuranAppBar
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//
//@Composable
//fun MainQuranHeader(){
//    MainQuranAppBar(Modifier)
//    LazyRow(
//        Modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(1) {
//            ItemMainQuranLastRead(Modifier, true)
//        }
//        items(5) {
//            ItemMainQuranLastRead(Modifier)
//        }
//    }
//    TargetKhattam(modifier = Modifier.padding(horizontal = 16.dp))
//    Spacer(Modifier.height(16.dp))
//}
//
//@Composable
//private fun TargetKhattam(modifier: Modifier) {
//    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
//        Column(Modifier.padding(16.dp)) {
//            Text(
//                "Target Khattam, Shawwal 1443",
//                fontFamily = lato,
//                fontSize = 16.sp,
//                color = colorResource(
//                    id = R.color.textPrimary
//                ),
//                fontWeight = FontWeight.Bold
//            )
//            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                Text(
//                    "Halaman 1 dari 604",
//                    fontFamily = lato,
//                    color = colorResource(id = R.color.textTertiary)
//                )
//                Text("0%", fontFamily = lato, color = colorResource(id = R.color.textTertiary))
//            }
//            Spacer(Modifier.height(8.dp))
//            LinearProgressIndicator(modifier = Modifier
//                .fillMaxWidth()
//                .height(6.dp),progress = 0.4f, color = colorResource(
//                id = R.color.colorPrimary
//            )
//            )
//        }
//    }
//}