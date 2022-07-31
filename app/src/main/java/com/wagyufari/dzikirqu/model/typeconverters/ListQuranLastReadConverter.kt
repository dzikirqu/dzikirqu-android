package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.QuranLastRead

class ListQuranLastReadConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<QuranLastRead> {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<List<QuranLastRead>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<QuranLastRead>): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}