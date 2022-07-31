package com.wagyufari.dzikirqu.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PrayerTime(var address: String? = null, var fajr: String?="", var sunrise: String? = "", var dhuhr: String? = "", var asr: String? = "", var sunset: String? = "", var maghrib: String? = "", var isya: String? = ""):
    Parcelable

