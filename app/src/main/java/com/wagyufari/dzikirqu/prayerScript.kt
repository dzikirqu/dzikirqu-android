package com.wagyufari.dzikirqu

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.model.PrayerBook
import com.wagyufari.dzikirqu.model.PrayerData

fun main() {
    changeIds()
}

fun changeIds(){
    val books = Gson().fromJson<ArrayList<PrayerBook>>(getJsonString("backup/book.json"), object : TypeToken<ArrayList<PrayerBook>>() {}.type)
    val prayer = Gson().fromJson<ArrayList<Prayer>>(getJsonString("backup/prayer.json"), object : TypeToken<ArrayList<Prayer>>() {}.type)
    val prayerData = Gson().fromJson<ArrayList<PrayerData>>(getJsonString("backup/prayerData.json"), object : TypeToken<ArrayList<PrayerData>>() {}.type)

    books.mapIndexed { index, book ->
        prayer.filter { it.book_id == book.id }.map {
            it.book_id = index.toString()
        }
        prayerData.filter { it.book_id == book.id }.map {
            it.book_id = index.toString()
        }
        book.id = index.toString()
    }
    prayer.mapIndexed { index, prayer ->
        prayerData.filter { it.prayer_id == prayer.id }.map {
            it.prayer_id = index.toString()
        }
        prayer.id = index.toString()
    }
    prayerData.mapIndexed { index, prayerData ->
        prayerData.id = index.toString()
    }

    createNewFile("prayerv2")
    writeToFile(Gson().toJson(prayer), "prayerv2")
    createNewFile("bookv2")
    writeToFile(Gson().toJson(books), "bookv2")
    createNewFile("prayerDatav2")
    writeToFile(Gson().toJson(prayerData), "prayerDatav2")
}