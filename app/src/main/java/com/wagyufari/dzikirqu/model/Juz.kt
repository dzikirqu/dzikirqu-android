package com.wagyufari.dzikirqu.model

data class Juz(var juz: Int, val surah: String, val surah_id:Int, val verse: Int, val start_page:Int, val end_page:Int, var isHizbShown:Boolean = false)