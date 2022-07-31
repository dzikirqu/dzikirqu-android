package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.LanguageString

class LanguageStringConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<LanguageString>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<ArrayList<LanguageString>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<LanguageString>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}