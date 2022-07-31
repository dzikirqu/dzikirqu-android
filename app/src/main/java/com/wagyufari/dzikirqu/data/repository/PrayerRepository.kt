package com.wagyufari.dzikirqu.data.repository

import com.wagyufari.dzikirqu.data.ApiService
import com.wagyufari.dzikirqu.data.room.dao.PrayerDao
import com.wagyufari.dzikirqu.data.room.dao.PrayerDataDao
import javax.inject.Inject


class PrayerRepository @Inject constructor(private val api:ApiService, private val dao:PrayerDao, private val dataDao: PrayerDataDao){

    // Prayer
    fun getPrayer(bookId:String) = dao.getPrayerByBookId(bookId)

    suspend fun getPrayerById(prayerId:String) = dao.getPrayerByIdSuspend(prayerId)
    // PrayerData
    fun getPrayerData(prayerId:String) = dataDao.getPrayerData(prayerId)

}