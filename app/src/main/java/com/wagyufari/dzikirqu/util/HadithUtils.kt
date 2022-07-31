package com.wagyufari.dzikirqu.util

import com.wagyufari.dzikirqu.model.Hadith
import com.wagyufari.dzikirqu.model.LanguageString

object HadithUtils {
    fun getHadiths(): ArrayList<Hadith> {
        return arrayListOf(
            Hadith(
                name = "Arba'in An Nawawi",
                count = 42,
                id = "nawawi40",
                author = "Imam An Nawawi",
                description = arrayListOf(
                    LanguageString(
                        text = "Arbain Nawawi or Al-Arba'in An-Nawawiyah (Arabic: الأربعون النووية) is a book that contains forty-two selected hadith compiled by Imam Nawawi. Arba'in means forty but actually there are forty-two hadith contained in this book. ",
                        language = "english"
                    ), LanguageString(
                        text = "Arbain Nawawi atau Al-Arba'in An-Nawawiyah (Arab:الأربعون النووية) merupakan kitab yang memuat empat puluh dua hadits pilihan yang disusun oleh Imam Nawawi. Arba'in berarti empat puluh namun sebenarnya terdapat empat puluh dua hadits yang termuat dalam kitab ini.",
                        language = "bahasa"
                    )
                )
            ), Hadith(
                name = "Riyad as-Salihin",
                count = 1896,
                id = "riyadussalihin",
                author = "Imam An Nawawi",
                metadata = "riyadussalihin_books_v3",
                description = arrayListOf(
                    LanguageString(
                        text = "Riyad as-Salihin or The Meadows of the Righteous, also referred to as The Gardens of the Righteous (Arabic: رياض الصالحين Riyāḍ aṣ-Ṣāliḥīn), is a compilation of verses from the Quran supplemented by hadith narratives written by Al-Nawawi from Damascus (1233–1277).",
                        language = "english"
                    ), LanguageString(
                        text = "Riyadhus Shalihin adalah nama salah satu kitab kumpulan hadis Nabi Muhammad ﷺ yang berarti taman orang-orang shalih, yang disusun oleh Imam Abu Zakariya Yahya bin Syaraf An-Nawawy (Imam Nawawi).",
                        language = "bahasa"
                    )
                )
            )
        )
    }
}