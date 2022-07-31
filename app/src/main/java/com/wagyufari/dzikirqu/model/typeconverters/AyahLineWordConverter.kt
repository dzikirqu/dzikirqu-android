package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.AyahLineWord

class AyahLineWordConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<AyahLineWord>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<List<AyahLineWord>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<AyahLineWord>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}