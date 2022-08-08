package com.wagyufari.dzikirqu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LanguageString(
    var text: String? = null,
    var language: String? = null,
    var counter: Int? = 0
):Parcelable