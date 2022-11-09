package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.model.Prayer

@Dao
interface PrayerDao {
    @Query("SELECT * FROM prayer WHERE book_id == :bookId ORDER BY `order`")
    fun getPrayerByBookId(bookId:String): LiveData<List<Prayer>>

    @Query("SELECT * FROM prayer WHERE book_id == :bookId ORDER BY `order`")
    suspend fun getPrayerByBookIdSuspend(bookId:String): List<Prayer>

    @Query("SELECT * FROM prayer WHERE book_id in (:bookIds) ORDER BY `order`")
    fun getPrayerByBookIds(bookIds:List<String>): LiveData<List<Prayer>>

    @Query("SELECT * FROM prayer ORDER BY `book_id`")
    fun getPrayer(): LiveData<List<Prayer>>

    @Query("SELECT * FROM prayer WHERE id in (:ids) ORDER BY `order`")
    fun getPrayerByIds(ids:List<String>): LiveData<List<Prayer>>
    @Query("SELECT * FROM prayer WHERE id in (:ids) ORDER BY `order`")
    suspend fun getPrayerByIdsSuspend(ids:List<String>): List<Prayer>

    @Query("SELECT * FROM prayer")
    suspend fun getPrayerSuspend(): List<Prayer>

    @Query("SELECT * FROM prayer WHERE id IN (:prayerId)")
    suspend fun getPrayerByIdSuspend(prayerId:String): List<Prayer>

    @Query("SELECT * FROM prayer WHERE id IS :prayerId")
    fun getPrayerById(prayerId:String): LiveData<Prayer?>

    @Query("SELECT * FROM prayer WHERE title LIKE :query")
    suspend fun searchPrayer(query:String): List<Prayer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPrayer(book:List<Prayer>)

    @Query("DELETE FROM prayer")
    suspend fun deletePrayer()
}


fun Context.getPrayerDao():PrayerDao{
    return PersistenceDatabase.getDatabase(this).prayerDao()
}