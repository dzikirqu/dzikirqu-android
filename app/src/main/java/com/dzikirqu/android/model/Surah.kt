package com.dzikirqu.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surah")
data class Surah(
    @PrimaryKey
    val id: Int,
    val revelation: String,
    val verses: Int,
    val name: String,
    val page:Int,
    val translation: ArrayList<LanguageString>
)