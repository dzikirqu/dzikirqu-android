package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.model.PrayerBook

@Dao
interface PrayerBookDao {
    @Query("SELECT * FROM prayerbooks")
    fun getBooks(): LiveData<List<PrayerBook>>

    @Query("SELECT * FROM prayerbooks")
    suspend fun getBooksSuspend(): List<PrayerBook>

    @Query("SELECT * FROM prayerbooks WHERE id == :id")
    suspend fun getBookById(id:String):List<PrayerBook>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putBook(book:PrayerBook)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putBooks(book:List<PrayerBook>)

    @Query("DELETE FROM prayerbooks")
    suspend fun deleteBooks()
}

fun Context.getBookDao():PrayerBookDao{
    return PersistenceDatabase.getDatabase(this).prayerBookDao()
}