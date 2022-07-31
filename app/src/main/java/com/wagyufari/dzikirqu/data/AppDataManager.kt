package com.wagyufari.dzikirqu.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.VersesItem
import com.wagyufari.dzikirqu.data.room.BookmarkDatabase
import com.wagyufari.dzikirqu.data.room.NotesDatabase
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.util.FileUtils
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper
import kotlinx.coroutines.Dispatchers.Main
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(
    val mContext: Context,
    val mApiService: ApiService,
    val mPersistenceDatabase: PersistenceDatabase,
    val mBookmarkDatabase: BookmarkDatabase,
    val mNoteDatabase: NotesDatabase,
) {

    fun getBooks(): LiveData<List<PrayerBook>>? {
        return mPersistenceDatabase.prayerBookDao().getBooks()
    }

    suspend fun getBookById(id: String): PrayerBook? {
        return mPersistenceDatabase.prayerBookDao().getBookById(id).firstOrNull()
    }

    fun getJuz(): ArrayList<Juz> {
        return Gson().fromJson(
            FileUtils.getJsonStringFromAssets(mContext, "json/quran/juz.json"),
            object : TypeToken<ArrayList<Juz>>() {}.type
        )
    }

    fun getQuranByPage(page: Int): ArrayList<VersesItem> {
        return Gson().fromJson(
            FileUtils.getJsonStringFromAssets(
                mContext,
                "json/quran/paged/page$page.json"
            ), object : TypeToken<ArrayList<VersesItem>>() {}.type
        )
    }

    fun isFirstAyahOfJuz(juz: Int, chapterId: Int, verseNumber: Int): Boolean {
        getJuz().filter { it.juz == juz }.firstOrNull()?.let {
            return it.surah_id == chapterId && it.verse == verseNumber
        }
        return false
    }

    fun getHizb(): List<Hizb> {
        return Gson().fromJson<ArrayList<Hizb>>(
            FileUtils.getJsonStringFromAssets(mContext, "json/quran/hizb.json"),
            object : TypeToken<ArrayList<Hizb>>() {}.type
        )
    }

    fun getHadithById(id: String): List<HadithData> {
        return Gson().fromJson<ArrayList<HadithData>>(
            FileUtils.getJsonStringFromAssets(mContext, "json/hadith/$id.json"),
            object : TypeToken<ArrayList<HadithData>>() {}.type
        )
    }

    fun getJuzWithHizb(juzNumber: Int): List<Any> {
        val juz = getJuz()
        val hizb = Gson().fromJson<ArrayList<Hizb>>(
            FileUtils.getJsonStringFromAssets(mContext, "json/quran/hizb.json"),
            object : TypeToken<ArrayList<Hizb>>() {}.type
        )
        return arrayListOf<Any>().apply {
            juz.forEach {
                this.add(it)
                if (it.juz == juzNumber) {
                    it.isHizbShown = true
                    this.addAll(hizb.filter { hizb ->
                        hizb.juz == it.juz
                    })
                }
            }
        }
    }


    fun getPrayer(bookId: String): LiveData<List<Prayer>> {
        return mPersistenceDatabase.prayerDao().getPrayerByBookId(bookId)
    }

    suspend fun getPrayerById(prayerId: String): Prayer? {
        return mPersistenceDatabase.prayerDao().getPrayerByIdSuspend(prayerId).firstOrNull()
    }

    fun getPrayerData(prayerId: String): LiveData<List<PrayerData>> {
        return mPersistenceDatabase.prayerDataDao().getPrayerData(prayerId)
    }

    private suspend fun getPrayerTime(): PrayerTime {
        return PrayerTimeHelper.getPrayerTime(mContext)
    }

    val prayerTime = liveData(Main) {
        try {
            val prefs = PrayerTimeHelper.getPrayerTimeFromPrefs(mContext)
            emit(prefs)
            val new = getPrayerTime()
            emit(new)
        } catch (e: java.lang.Exception) {
        }
    }

    fun getGregorianDate(): String {
        return SimpleDateFormat("EEE, MMM d, yyyy").format(Calendar.getInstance().time)
    }

    fun getHijriDate(): String {
        val cal = UmmalquraCalendar()
        cal.time = Calendar.getInstance().time
        return "${cal.get(Calendar.DAY_OF_MONTH)} ${
            cal.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH)}," +
                " ${cal.get(Calendar.YEAR)}"
    }


}