//package com.wagyufari.dzikirqu.ui.v2.main.quran
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.colorResource
//import com.google.accompanist.pager.ExperimentalPagerApi
//import com.wagyufari.dzikirqu.R
//import com.wagyufari.dzikirqu.ui.v2.main.quran.composable.MainQuranTab
//import com.wagyufari.dzikirqu.ui.v2.main.quran.composable.header.MainQuranHeader
//import com.wagyufari.dzikirqu.ui.v2.utils.CollapsableToolbar
//import com.wagyufari.dzikirqu.ui.v2.utils.MotionComposeHeader
//import com.wagyufari.dzikirqu.ui.v2.utils.SwipingStates
//
//@OptIn(ExperimentalPagerApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
//@Composable
//fun MainQuranScreen(viewModel: MainQuranV2ViewModel) {
//
//    val surah by remember{
//        viewModel.surah
//    }
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(colorResource(id = R.color.neutral_50)),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        CollapsableToolbar { swipingState ->
//            MotionComposeHeader(progress = if (swipingState.progress.to == SwipingStates.COLLAPSED) swipingState.progress.fraction else 1f - swipingState.progress.fraction, header = {
//                MainQuranHeader()
//            }, content = {
//                MainQuranTab(surah)
//            })
//        }
//    }
//}
