package com.wagyufari.dzikirqu.data.repository

import com.wagyufari.dzikirqu.data.ApiService
import com.wagyufari.dzikirqu.data.room.dao.PrayerBookDao
import javax.inject.Inject


class PrayerBookRepository @Inject constructor(private val api:ApiService, private val dao:PrayerBookDao){

    val books = dao.getBooks()

    suspend fun getBookById(id:String) = dao.getBookById(id)
}