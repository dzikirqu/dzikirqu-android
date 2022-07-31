package com.wagyufari.dzikirqu

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.model.Ayah
import com.wagyufari.dzikirqu.model.LanguageString
import java.io.*

fun main() {
    saveQuran("")
}

fun saveQuran(name: String) {
    val quran = File("quran.txt").bufferedReader().readLines().map {
        AyahData(it.split("|")[2], it.split("|")[1].toInt(), it.split("|").first().toInt())
    }
    for (i in 1..6){
        getLegacyQuran(i).writeQuran(quran, i)
    }
}

fun getLegacyQuran(number:Int):ArrayList<Ayah>{
    return Gson().fromJson(
        getJsonString("quran$number.json"),
        object : TypeToken<ArrayList<Ayah>>() {}.type
    )
}

fun ArrayList<Ayah>.writeQuran(quran: List<AyahData>, part: Int) {
    val result = map {
        it.apply {
            text.removeAll { it.language == "arabic" }
            text.add(LanguageString(text = quran.filter { it.ayah == this.verse_number && it.surah == this.chapterId }
                .first().text
                .replace(",", "")
                .replace(
                    if (it.verse_number == 1 && it.chapterId != 1) "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ" else "",
                    ""
                )
                .replace("و۟","و")
                .replace("ا۟", "ا")
                .replace("ي۟", "ي"), language = "arabic"))
        }
    }
    writeToFile(Gson().toJson(result), "quran$part")
}

data class AyahData(val text: String, val ayah: Int, val surah: Int)

