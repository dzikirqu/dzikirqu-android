package com.dzikirqu.android.model

import java.util.*
import kotlin.collections.ArrayList

data class QuranTrackData(
    val date:Date,
    val sessions: ArrayList<Session>
){
    data class Session(var startingSurah:Int,
                       var startingAyah:Int,
                       var lastSurah:Int,
                       var lastAyah:Int)
}