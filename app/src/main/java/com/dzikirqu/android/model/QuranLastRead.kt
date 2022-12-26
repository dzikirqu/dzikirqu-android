package com.dzikirqu.android.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "quranLastRead")
@Parcelize
data class QuranLastRead(
    @PrimaryKey
    var surah:Int,
    var ayah:Int,
    var page:Int?=null,
    var isSavedFromPage:Boolean?=false,
    var timestamp:Long?=0
):Parcelable{
    var lap:Int= 0
    var state:String = KhatamStateConstants.ACTIVE
}

data class QuranLastReadString(
    val surah:String,
    val ayah:String,
    val unicode:String
)