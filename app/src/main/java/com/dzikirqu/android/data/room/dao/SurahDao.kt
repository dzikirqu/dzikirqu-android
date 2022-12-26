package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.model.Surah

@Dao
interface SurahDao {

    @Query("SELECT * FROM surah")
    suspend fun getSurah(): List<Surah>
    @Query("SELECT * FROM surah WHERE id is :id")
    suspend fun getSurahById(id:Int): List<Surah>
    @Query("SELECT * FROM surah WHERE id is :id")
    fun getSurahByIdLive(id:Int): LiveData<Surah?>
    @Query("SELECT * FROM surah WHERE id is :id")
    suspend fun getSurahByIdSingle(id:Int):Surah?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putSurah(book:List<Surah>)
    @Query("DELETE FROM surah")
    suspend fun deleteSurah()
}

fun Context.getSurahDao():SurahDao{
    return PersistenceDatabase.getDatabase(this).surahDao()
}
fun Fragment.getSurahDao():SurahDao{
    return PersistenceDatabase.getDatabase(requireActivity()).surahDao()
}