package com.wagyufari.dzikirqu.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wagyufari.dzikirqu.model.HadithData

@Dao
interface HadithDataDao {

    @Query("SELECT * FROM hadithData WHERE hadith_id IS :hadithId")
    suspend fun getHadithById(hadithId:String): List<HadithData>

    @Query("SELECT * FROM hadithData")
    suspend fun getHadith(): List<HadithData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putHadithData(book:List<HadithData>)

    @Query("DELETE FROM hadithData")
    suspend fun deleteHadithData()
}