package com.dzikirqu.android.model.search

import com.dzikirqu.android.model.Ayah

data class SearchQuranModel(
    val surah:Int,
    val ayah:List<Ayah>
)