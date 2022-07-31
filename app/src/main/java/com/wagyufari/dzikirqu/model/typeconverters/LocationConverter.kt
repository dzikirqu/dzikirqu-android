package com.wagyufari.dzikirqu.model.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.wagyufari.dzikirqu.model.Location

class LocationConverter {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): Location? {
        val gson = Gson()
        if (data == null) {
            return null
        }

        val speaker = try{
            gson.fromJson(data, Location::class.java)
        } catch (e:Exception){
            Location(name = data)
        }
        return speaker
    }

    @TypeConverter
    fun someObjectListToString(someObjects: Location?): String {
        val gson = Gson()
        return gson.toJson(someObjects)
    }
}