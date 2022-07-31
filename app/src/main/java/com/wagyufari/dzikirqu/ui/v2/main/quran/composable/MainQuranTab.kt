//package com.wagyufari.dzikirqu.ui.v2.main.quran.composable
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.Tab
//import androidx.compose.material.TabRow
//import androidx.compose.material.TabRowDefaults
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.google.accompanist.pager.ExperimentalPagerApi
//import com.google.accompanist.pager.HorizontalPager
//import com.google.accompanist.pager.pagerTabIndicatorOffset
//import com.google.accompanist.pager.rememberPagerState
//import com.wagyufari.dzikirqu.model.Surah
//import com.wagyufari.dzikirqu.ui.v2.main.quran.composable.items.ItemSurah
//import com.wagyufari.dzikirqu.ui.v2.theme.lato
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun MainQuranTab(surah:List<Surah>) {
//    val tabTitles = listOf("Surah", "Juz")
//    val pagerState = rememberPagerState() // 2.
//
//    Column {
//        TabRow(
//            modifier = Modifier.background(Color.White),
//            selectedTabIndex = pagerState.currentPage,
//            indicator = { tabPositions -> // 3.
//                TabRowDefaults.Indicator(
//                    Modifier.pagerTabIndicatorOffset(
//                        pagerState,
//                        tabPositions
//                    )
//                )
//            },
//            backgroundColor = Color.White
//        ) {
//            tabTitles.forEachIndexed { index, title ->
//                Tab(selected = pagerState.currentPage == index,
//                    onClick = {
//                        CoroutineScope(Dispatchers.Main).launch {
//                            pagerState.scrollToPage(index)
//                        }
//                    },
//                    text = { Text(text = title, fontFamily = lato) })
//            }
//        }
//        HorizontalPager(
//            // 4.
//            count = tabTitles.size,
//            state = pagerState,
//        ) { tabIndex ->
//            LazyColumn(
//                Modifier.padding(
//                    bottom = 56.dp // We need to reduce content height by the height of collapsed content
//                ).background(Color.White).fillMaxSize()
//            ) {
//                itemsIndexed(
//                    items = surah,
//                    itemContent = { index, item ->
//                        ItemSurah(item, index+1)
//                    }
//                )
//            }
//        }
//    }
//}