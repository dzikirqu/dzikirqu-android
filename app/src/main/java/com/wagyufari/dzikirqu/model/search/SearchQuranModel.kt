package com.wagyufari.dzikirqu.model.search

import com.wagyufari.dzikirqu.model.Ayah

data class SearchQuranModel(
    val surah:Int,
    val ayah:List<Ayah>
)