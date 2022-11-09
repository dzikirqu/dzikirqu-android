package com.dzikirqu.android.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.model.QuranLastRead

class QuranLastReadConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): QuranLastRead {
        val gson = Gson()
        if (data == null) {
            return QuranLastRead(
                surah = 0,
                ayah = 0,
                page = null,
                isSavedFromPage = null,
                timestamp = null
            )
        }

        val listType = object : TypeToken<QuranLastRead>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: QuranLastRead): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}