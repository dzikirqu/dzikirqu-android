package com.wagyufari.dzikirqu.data.room.dao

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import com.wagyufari.dzikirqu.model.AyahLine

@Dao
interface AyahLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAyahLine(book:List<AyahLine>)

    @Query("SELECT * FROM ayahline")
    suspend fun getAyahLine(): List<AyahLine>

    @Query("SELECT * FROM ayahline WHERE page IS :page")
    suspend fun getAyahLineByPage(page:Int): List<AyahLine>

    @Query("SELECT * FROM ayahline WHERE verse_key IS :verseKey")
    suspend fun getAyahLineByKey(verseKey:String): List<AyahLine>

    @Query("SELECT * FROM ayahline WHERE verse_key IS :verseKey")
    fun getAyahLineByKeyLive(verseKey:String): LiveData<List<AyahLine>>

    @Query("SELECT * FROM ayahline WHERE juz_number IS :juzNumber")
    suspend fun getAyahLineByJuzNumber(juzNumber:Int): List<AyahLine>

    @Query("SELECT * FROM ayahline WHERE hizb_number IS :hizb")
    suspend fun getAyahLineByHizbNumber(hizb:Float): List<AyahLine>

    @Query("SELECT COUNT() FROM ayahline")
    suspend fun getCount():Int

    @Query("DELETE FROM ayahline")
    suspend fun delete()
}

fun Context.getAyahLineDao():AyahLineDao{
    return PersistenceDatabase.getDatabase(this).ayahLineDao()
}
fun Fragment.getAyahLineDao():AyahLineDao{
    return PersistenceDatabase.getDatabase(requireActivity()).ayahLineDao()
}