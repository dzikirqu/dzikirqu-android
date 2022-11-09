package com.dzikirqu.android

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.model.HadithChapter
import com.dzikirqu.android.model.HadithData
import com.dzikirqu.android.model.HadithSubChapter
import com.dzikirqu.android.model.LanguageString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

fun main() {
    saveHadith("")
}

fun getChapterIndexes(url:String):ChapterIndex{
    val jsoup = Jsoup.connect(url)
        .get()

    val chapters = jsoup.select("a").map { it.toString() }
        .filter { it.contains(".00") }
    val data = jsoup.select("a").map { it.toString() }
        .filter { it.contains("Riyad as-Sal") || it.contains(".00") }

    val firstIndexes = chapters.mapIndexed{ index, s ->
        val firstIndex = data.indexOfFirst { it.contains(s) } + 1
        if (data[firstIndex].contains(".00")){
            return@mapIndexed ""
        }
        val startIndex = data[firstIndex].substring(data[firstIndex].indexOfFirst { it == ':' }, data[firstIndex].indexOfLast { it == '"' })
        return@mapIndexed startIndex.replace(":", "")
    }.map { it.toIntOrNull() }.toCollection(arrayListOf())
    val lastIndexes = chapters.mapIndexed { index, s ->
        if (chapters.count() != index + 1) {
            val lastIndex = data.indexOfFirst { it.contains(chapters[index + 1]) } - 1
            if (data[lastIndex].contains(".00")){
                return@mapIndexed ""
            }
            val endIndex = data[lastIndex].substring(data[lastIndex].indexOfFirst { it == ':' }, data[lastIndex].indexOfLast { it == '"' })
            return@mapIndexed endIndex.replace(":", "")
        } else{
            val lastIndex = data.count()-1
            if (data[lastIndex].contains(".00")){
                return@mapIndexed ""
            }
            val endIndex = data[lastIndex].substring(data[lastIndex].indexOfFirst { it == ':' }, data[lastIndex].indexOfLast { it == '"' })
            return@mapIndexed endIndex.replace(":", "")
        }
    }.map { it.toIntOrNull() }.toCollection(arrayListOf())
    return ChapterIndex(firstIndexes, lastIndexes)
}

data class ChapterIndex(val firstIndex:ArrayList<Int?>, val lastIndex:ArrayList<Int?>)

fun getChapter(url: String): List<HadithSubChapter> {
    val jsoup = Jsoup.connect(url)
        .get()

    val index = jsoup.select("div").filter { it.hasClass("echapno") }.map { it.text() }
    val title = jsoup.select("div").filter { it.hasClass("englishchapter") }.map { it.text() }
    val arabic = jsoup.select("div").filter { it.hasClass("arabicchapter arabic") }.map { it.text() }

    val (startIndex, endIndex) = getChapterIndexes(url)
    println(url)
    return index.mapIndexed { i, element ->
        HadithSubChapter(
            title = arrayListOf(LanguageString(title[i], "english"),LanguageString(arabic[i], "arabic")),
            chapterIndex = index[i].replace("(", "").replace(")", "").toInt(),
            startIndex = startIndex[i],
            endIndex = endIndex[i]
        )
    }
}

fun saveBookV2() {
    val books = Gson().fromJson<ArrayList<HadithChapter>>(
        File("riyadussalihin_books.json").inputStream().readBytes().toString(Charsets.UTF_8),
        object : TypeToken<ArrayList<HadithChapter>>() {}.type
    )

    val bookV2 = books.mapIndexed { index, hadithBook ->
        hadithBook.apply {
            if (index == 0) {
                chapters.addAll(getChapter("https://sunnah.com/riyadussalihin/introduction"))
            } else {
                chapters.addAll(getChapter("https://sunnah.com/riyadussalihin/$index"))
            }
        }
    }

    writeToFile(Gson().toJson(bookV2), "riyadussalihin_books_v2")
}

fun saveBook() {
    val jsoup = Jsoup.connect("https://sunnah.com/riyadussalihin")
        .get()

    val english =
        jsoup.select("div").filter { it.hasClass("english english_book_name") }.map { it.text() }
    val arabic =
        jsoup.select("div").filter { it.hasClass("arabic arabic_book_name") }.map { it.text() }
    val startIndex = jsoup.select("div").filter { it.hasClass("book_range") }.map {
        it.text().split(" ")[0]
    }
    val endIndex = jsoup.select("div").filter { it.hasClass("book_range") }.map {
        it.text().split(" ")[2]
    }

    val books = english.mapIndexed { index, s ->
        HadithChapter(
            title = arrayListOf(
                LanguageString(s, "english"),
                LanguageString(arabic[index], "arabic")
            ),
            startIndex = startIndex[index].toInt(),
            endIndex = endIndex[index].toInt(),
            chapters = arrayListOf()
        )
    }

    writeToFile(Gson().toJson(books), "riyadussalihin_books")
}

fun saveHadith(name: String) {
    val bahasa = getBahasa(
        Jsoup.parse(
            File("bahasahtml.json").inputStream().readBytes().toString(Charsets.UTF_8)
        )
    )
    val main = getMain(
        Jsoup.parse(
            File("englishhtml.json").inputStream().readBytes().toString(Charsets.UTF_8)
        )
    )

    writeToFile(Gson().toJson(main.mapIndexed { index, hadithData ->
        hadithData.text.add(bahasa[index].text.first())
        hadithData.narrated.add(bahasa[index].narrated.first())
        hadithData
    }), "riyadussalihin17")
}

var currentIndex = 1511

fun getMain(document: Document): List<HadithData> {
    val targetElement = document.body()
        .children()
    val hadithRaw = targetElement.select("div")
        .filter { it.hasClass("actualHadithContainer hadith_container_riyadussalihin") }

    val hadiths = hadithRaw.mapIndexed { index, element ->
        val translation = element.select("div").filter { it.hasClass("text_details") }.map {
            it.text()
        }
        val narrated = element.select("div").filter { it.hasClass("hadith_narrated") }.map {
            it.text()
        }
        val arabic = element.select("div").filter { it.hasClass("arabic_hadith_full arabic") }.map {
            it.text()
        }.map {
            it.replace(",", "")
        }

        HadithData(
            (index + currentIndex).toString(),
            "riyadussalihin",
            arrayListOf(
                LanguageString(arabic.firstOrNull(), "arabic"),
                LanguageString(translation.firstOrNull(), "english"),
            ),
            arrayListOf(
                LanguageString(narrated.firstOrNull(), "english"),
            )
        )
    }

    return hadiths
}

fun getBahasa(document: Document): List<HadithData> {
    val targetElement = document.body()
        .children()
    val hadithRaw = targetElement.select("div")
        .filter { it.hasClass("actualHadithContainer hadith_container_riyadussalihin") }

    val hadiths = hadithRaw.mapIndexed { index, element ->
        val translation = element.select("div").filter { it.hasClass("text_details") }.map {
            it.text()
        }
        val narrated = element.select("div").filter { it.hasClass("hadith_narrated") }.map {
            it.text()
        }

        HadithData(
            (index + 1).toString(),
            "riyadussalihin",
            arrayListOf(
                LanguageString(translation.firstOrNull(), "bahasa"),
            ),
            arrayListOf(
                LanguageString(narrated.firstOrNull(), "bahasa"),
            )
        )
    }

    return hadiths
}