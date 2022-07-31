//package com.wagyufari.dzikirqu.ui.v2.main.quran.composable
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Card
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//import com.wagyufari.dzikirqu.ui.v2.theme.surah
//
//
//@Composable
//fun ItemMainQuranLastRead(modifier: Modifier, isLastRead: Boolean? = false) {
//    Card(
//        modifier = modifier,
//        shape = RoundedCornerShape(8.dp),
//        backgroundColor = if (isLastRead == true) colorResource(
//            id = R.color.colorPrimary
//        ) else Color.White
//    ) {
//        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
//            if (isLastRead == true) {
//                Image(
//                    modifier = Modifier
//                        .width(24.dp)
//                        .height(24.dp),
//                    painter = painterResource(id = R.drawable.ic_attachment),
//                    contentDescription = null,
//                    colorFilter = ColorFilter.tint(Color.White)
//                )
//                Spacer(Modifier.width(8.dp))
//            }
//            Column {
//                Text(
//                    "An Naml", fontFamily = lato,
//                    color = if (isLastRead == true) Color.White else colorResource(id = R.color.textPrimary)
//                )
//                Text(
//                    "Ayat 24",
//                    fontFamily = lato,
//                    fontSize = 12.sp,
//                    color = if (isLastRead == true) Color.White else colorResource(id = R.color.textTertiary)
//                )
//            }
//            Spacer(Modifier.width(16.dp))
//            Text(
//                "\uE920", fontFamily = surah, fontSize = 18.sp,
//                color = if (isLastRead == true) Color.White else colorResource(id = R.color.textPrimary)
//            )
//        }
//    }
//}
