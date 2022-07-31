package com.wagyufari.dzikirqu.model.response

import com.google.gson.annotations.SerializedName

data class DataModel<out T>(
        @SerializedName("data") val data: T? = null,
        @SerializedName("next_cursor") val nextCursor: Int? = 0,
        @SerializedName("use_tfa") val use_tfa: Boolean? = false
)