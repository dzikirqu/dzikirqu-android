package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.model.Ayah

@Dao
interface AyahDao {
    @Query("SELECT * FROM ayah WHERE chapter_id IS :chapterId")
    suspend fun getAyahBySurahId(chapterId:Int): List<Ayah>

    @Query("SELECT * FROM ayah WHERE chapter_id IS :chapterId AND verse_number IS :verseNumber")
    suspend fun getAyahBySurahIdAndVerseNumber(chapterId: Int, verseNumber:Int): List<Ayah>

    @Query("SELECT * FROM ayah")
    suspend fun getAyah(): List<Ayah>

    @Query("SELECT * FROM ayah WHERE text LIKE :query")
    suspend fun searchAyah(query:String): List<Ayah>

    @Query("Select count() from ayah")
    suspend fun getAyahCount():Int

    @Query("SELECT * FROM ayah WHERE juz IS :juz LIMIT :limit")
    suspend fun getAyahByJuz(juz:Int, limit:Int): List<Ayah>

    @Query("SELECT * FROM ayah WHERE hizb IS :hizb LIMIT :limit")
    suspend fun getAyahByHizb(hizb:Float, limit:Int): List<Ayah>

    @Query("SELECT * FROM ayah WHERE id IS :id")
    suspend fun getAyahById(id:Int): Ayah?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAyah(book:List<Ayah>)

    @Query("DELETE FROM ayah")
    suspend fun deleteAyah()
}

fun Context.getAyahDao():AyahDao{
    return PersistenceDatabase.getDatabase(this).ayahDao()
}
fun Fragment.getAyahDao():AyahDao{
    return PersistenceDatabase.getDatabase(requireActivity()).ayahDao()
}