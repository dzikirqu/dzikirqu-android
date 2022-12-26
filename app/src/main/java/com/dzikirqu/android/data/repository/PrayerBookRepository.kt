package com.dzikirqu.android.data.repository

import com.dzikirqu.android.data.ApiService
import com.dzikirqu.android.data.room.dao.PrayerBookDao
import javax.inject.Inject


class PrayerBookRepository @Inject constructor(private val api:ApiService, private val dao:PrayerBookDao){

    val books = dao.getBooks()

    suspend fun getBookById(id:String) = dao.getBookById(id)
}