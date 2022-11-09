package com.dzikirqu.android.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "prayerBooks")
@Parcelize
data class PrayerBook(
    @PrimaryKey
    var id:String,
    @ColumnInfo(name = "title")
    var title: ArrayList<LanguageString> = ArrayList(),
    @ColumnInfo(name = "description")
    var description:ArrayList<LanguageString> = ArrayList(),
    @ColumnInfo(name = "type")
    var type: String? = "prayer"
) :Parcelable