package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringArrayConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<String>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<ArrayList<String>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<String>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}