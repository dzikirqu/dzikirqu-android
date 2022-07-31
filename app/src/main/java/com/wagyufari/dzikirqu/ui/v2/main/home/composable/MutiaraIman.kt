//package com.wagyufari.dzikirqu.ui.v2.main.home.composable
//
//import android.content.ActivityNotFoundException
//import android.content.Intent
//import android.net.Uri
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Card
//import androidx.compose.material.ExperimentalMaterialApi
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun MutiaraIman(modifier: Modifier) {
//    val context = LocalContext.current
//
//    Card(modifier = modifier.padding(8.dp), shape = RoundedCornerShape(12.dp), onClick = {
//        val uri: Uri = Uri.parse("https://www.instagram.com/_mutiaraiman_/")
//        val likeIng = Intent(Intent.ACTION_VIEW, uri)
//        likeIng.setPackage("com.instagram.android")
//        try {
//            context.startActivity(likeIng)
//        } catch (e: ActivityNotFoundException) {
//            context.startActivity(
//                Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://www.instagram.com/_mutiaraiman_/")
//                )
//            )
//        }
//    }) {
//        Row(
//            modifier = Modifier.padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Image(
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .width(32.dp)
//                        .height(32.dp),
//                    painter = painterResource(id = R.drawable.mutiaraimanpp),
//                    contentDescription = null
//                )
//                Spacer(Modifier.width(12.dp))
//                Column {
//                    Text("Mutiara Iman", fontFamily = lato, fontWeight = FontWeight.Bold)
//                    Text("Mutiara Iman", fontFamily = lato, fontSize = 12.sp)
//                }
//            }
//            Image(
//                modifier = Modifier
//                    .width(24.dp)
//                    .height(24.dp),
//                painter = painterResource(id = R.drawable.ic_instagram),
//                contentDescription = null,
//                colorFilter = ColorFilter.tint(colorResource(id = R.color.neutral_400))
//            )
//        }
//    }
//}
