package com.wagyufari.dzikirqu.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "bookmarks")
@Parcelize
data class Bookmark(
    @PrimaryKey(autoGenerate = false)
    val id:Int?=null,
    @ColumnInfo(name = "idString")
    var idString: String?=null,
    @ColumnInfo(name = "type")
    var type:String = BookmarkType.QURAN,
    @ColumnInfo(name = "highlighted")
    var highlighted:Boolean = false,
    @ColumnInfo(name = "group_id")
    var groupId:Int?=null
) :Parcelable

@Entity(tableName = "bookmarkGroup")
@Parcelize
data class BookmarkGroup(
    @PrimaryKey(autoGenerate = false)
    val id:Int?=null,
    @ColumnInfo(name = "title")
    var title: String?=null,
    @ColumnInfo(name = "type")
    var type:String?=null
) :Parcelable

data class BookmarkHighlightUpdate(val id:Int, val highlighted: Boolean)

object BookmarkType{
    const val QURAN = "quran"
    const val QURANPAGE = "quranPage"
    const val HADITH = "hadith"
    const val PRAYER = "prayer"
}

object QuranBookmarkIdIndex{
    const val SURAH_ID = 0
    const val AYAH_ID = 1
}

object PrayerBookmarkIdIndex{
    const val BOOK_ID = 0
    const val PRAYER_ID = 1
}