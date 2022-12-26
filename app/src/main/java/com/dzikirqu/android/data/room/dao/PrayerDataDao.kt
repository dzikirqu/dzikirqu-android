package com.dzikirqu.android.data.room.dao

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.model.PrayerData

@Dao
interface PrayerDataDao {
    @Query("SELECT * FROM prayerData WHERE prayer_id == :prayerId ORDER BY `order` ASC")
    fun getPrayerData(prayerId:String): LiveData<List<PrayerData>>

    @Query("SELECT * FROM prayerData")
    fun getAllPrayerData():LiveData<List<PrayerData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putPrayerData(book:List<PrayerData>)

    @Query("SELECT * FROM prayerData WHERE text LIKE :query")
    fun getQueryPrayerData(query:String): LiveData<List<PrayerData>>

    @Query("DELETE FROM prayerData")
    suspend fun deletePrayerData()
}

fun Context.getPrayerDataDao():PrayerDataDao{
    return PersistenceDatabase.getDatabase(this).prayerDataDao()
}