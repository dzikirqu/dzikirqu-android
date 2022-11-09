package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.model.QuranLastRead

@Dao
interface QuranLastReadDao {
    @Query("SELECT * FROM quranlastread ORDER BY timestamp DESC")
    fun getQuranLastRead(): LiveData<List<QuranLastRead>>
    @Query("SELECT * FROM quranlastread ORDER BY timestamp DESC")
    suspend fun getQuranLastReadSuspend(): List<QuranLastRead>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putQuranLastRead(data:QuranLastRead)
    @Query("DELETE FROM quranlastread WHERE isSavedFromPage IS :isSavedFromPage")
    suspend fun deleteSavedFromPage(isSavedFromPage:Boolean?=true)
}

fun Context.getQuranLastReadDao():QuranLastReadDao{
    return PersistenceDatabase.getDatabase(this).quranLastReadDao()
}
fun Fragment.getQuranLastReadDao():QuranLastReadDao{
    return PersistenceDatabase.getDatabase(requireActivity()).quranLastReadDao()
}