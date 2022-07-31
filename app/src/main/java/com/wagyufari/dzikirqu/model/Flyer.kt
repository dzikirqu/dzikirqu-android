package com.wagyufari.dzikirqu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Flyer(
    val id: String,
    var title: String? = null,
    var caption: String? = null,
    var image: String? = "",
    var language: String? = "bahasa"
) : Parcelable