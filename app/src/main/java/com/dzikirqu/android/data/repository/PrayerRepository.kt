package com.dzikirqu.android.data.repository

import com.dzikirqu.android.data.ApiService
import com.dzikirqu.android.data.room.dao.PrayerDao
import com.dzikirqu.android.data.room.dao.PrayerDataDao
import javax.inject.Inject


class PrayerRepository @Inject constructor(private val api:ApiService, private val dao:PrayerDao, private val dataDao: PrayerDataDao){

    // Prayer
    fun getPrayer(bookId:String) = dao.getPrayerByBookId(bookId)

    suspend fun getPrayerById(prayerId:String) = dao.getPrayerByIdSuspend(prayerId)
    // PrayerData
    fun getPrayerData(prayerId:String) = dataDao.getPrayerData(prayerId)

}