package com.dzikirqu.android.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "prayer")
data class Prayer(
    @PrimaryKey
    var id:String,
    var book_id: String?= null,
    var order: Int?=null,
    var title: ArrayList<LanguageString>? = null
)

@Entity(tableName = "prayerData")
@Parcelize
data class PrayerData(
    @PrimaryKey
    var id:String="null",
    var book_id: String?=null,
    var prayer_id:String?=null,
    var order:Int?=null,
    var text:ArrayList<LanguageString>?=null,
    var source: ArrayList<LanguageString>? = null,
    var notes: ArrayList<LanguageString>? = arrayListOf(),
    var audio: String? = null,
    var link:Link? = null
):Parcelable

@Parcelize
data class Link(
    val title:ArrayList<LanguageString>?=null,
    val subtitle:ArrayList<LanguageString>?=null,
    val description: String?=null,
    val type: LinkType?=null,
    val link:String?=null
):Parcelable

enum class LinkType{
    Quran,
    Unidentified
}