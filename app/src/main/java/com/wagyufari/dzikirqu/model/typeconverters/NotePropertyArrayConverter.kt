package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.NoteProperty

class NotePropertyArrayConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<NoteProperty>? {
        val gson = Gson()
        if (data == null) {
            return arrayListOf()
        }

        val listType = object : TypeToken<ArrayList<NoteProperty>?>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<NoteProperty>?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}