package com.dzikirqu.android.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.model.Link
import com.dzikirqu.android.model.LinkType

class LinkConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Link? {
        val gson = Gson()
        if (data == null) {
            return Link(arrayListOf(),arrayListOf(),"",LinkType.Unidentified, "")
        }

        val listType = object : TypeToken<Link>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Link?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}