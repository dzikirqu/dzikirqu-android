package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.Author

class AuthorTypeConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Author? {
        val gson = Gson()
        if (data == null) {
            return Author(uid = "", name = "", profilePicture = "")
        }

        val listType = object : TypeToken<Author>() {
        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Author?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}