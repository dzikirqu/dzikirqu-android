package com.dzikirqu.android.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dzikirqu.android.data.room.dao.getPrayerDao
import com.dzikirqu.android.model.Bookmark
import com.dzikirqu.android.model.LanguageString
import com.dzikirqu.android.ui.main.prayer.PrayerType
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.util.StringExt.getText
import com.dzikirqu.android.R

val prayerTypes = arrayListOf(
    PrayerType(
        title = arrayListOf(LanguageString(text = "Semua", language = "bahasa"),
            LanguageString(text = "All", language = "english")),
        ids = listOf(),
        image = R.drawable.ic_grid
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Dzikir Harian", language = "bahasa"),
            LanguageString(text = "Daily Dzikir", language = "english")),
        ids = listOf(28, 5, 29, 34),
        image = R.drawable.ic_dzikir
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Do'a dari Al-Qur'an", language = "bahasa"),
            LanguageString(text = "Prayers from Al-Qur'an", language = "english")),
        ids = listOf(18,
            41,
            27,
            44,
            38,
            7,
            23,
            36,
            11,
            24,
            45,
            37,
            46,
            50,
            1,
            12,
            47,
            51,
            19,
            0,
            6),
        image = R.drawable.ic_quran
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Do'a Sehari-hari", language = "bahasa"),
            LanguageString(text = "Daily Prayer", language = "english")),
        ids = listOf(53,
            22,
            30,
            39,
            3,
            4,
            31,
            42,
            20,
            2,
            8,
            15,
            52,
            32,
            48,
            33,
            40,
            49,
            9,
            25,
            26,
            35,
            10,
            21,
            16,
            13,
            14,
            43,
            17,
            54,
            55,
            56,
            57,
            58,
            59,
            60,
            61,
            62,
            63,
            64,
            65,
            66,
            67,
            68,
            69,
            70,
            71,
            72,
            73,
            74,
            75,
            76,
            77,
            78,
            79,
            80,
            81,
            82,
            83,
            84,
            85,
            86,
            87,
            88,
            89,
            90,
            91,
            92,
            93,
            94,
            95,
            96,
            97,
            98,
            99,
            100,
            101,
            102,
            103,
            104,
            105,
            106,
            107,
            108),
        image = R.drawable.ic_book
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Masjid", language = "bahasa"),
            LanguageString(text = "Masjid", language = "english")),
        ids = listOf(32, 48, 33, 40, 49),
        image = R.drawable.ic_mosque
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Shalat", language = "bahasa"),
            LanguageString(text = "Shalat", language = "english")),
        ids = listOf(9, 25, 26, 35, 10, 21, 16, 14, 43, 17, 54, 29),
        image = R.drawable.ic_shalat
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Pakaian", language = "bahasa"),
            LanguageString(text = "Clothing", language = "english")),
        ids = listOf(42, 20, 2, 8),
        image = R.drawable.ic_checkroom
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Tidur", language = "bahasa"),
            LanguageString(text = "Sleeping", language = "english")),
        ids = listOf(34, 53, 22, 30),
        image = R.drawable.ic_bed
    ),
    PrayerType(
        title = arrayListOf(LanguageString(text = "Pengantin", language = "bahasa"),
            LanguageString(text = "Newlywed", language = "english")),
        ids = listOf(55, 56, 57),
        image = R.drawable.ic_ring
    ),
)

object PrayerExt {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun highlighted(bookmark: Bookmark) {
        val context = LocalContext.current
        LocalContext.current.getPrayerDao()
            .getPrayerById(bookmark.idString.toString())
            .observeAsState().value?.let { prayer ->
                Card(modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
                    backgroundColor = colorResource(
                        id = R.color.neutral_0),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        ReadActivity.startPrayer(context, prayer)
                    }) {
                    Row(modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier
                            .clip(RoundedCornerShape(16))
                            .background(colorResource(id = R.color.colorPrimary))) {
                            Image(modifier = Modifier
                                .height(24.dp)
                                .width(24.dp)
                                .padding(2.dp),
                                painter = painterResource(id = R.drawable.ic_attachment),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(colorResource(id = R.color.white)))
                        }
                        horizontalSpacer(width = 8)
                        Text(modifier = Modifier.widthIn(20.dp, 180.dp),
                            text = prayer.title?.getText() ?: "",
                            fontFamily = lato,
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = colorResource(id = R.color.textPrimary),
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }

    }

}