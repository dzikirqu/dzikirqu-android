package com.wagyufari.dzikirqu.data.repository

import android.content.Context
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import com.wagyufari.dzikirqu.data.room.dao.SurahDao
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.util.getPath1FromUrl
import javax.inject.Inject


class SurahRepository @Inject constructor(private val dao:SurahDao){

    companion object{
        fun getInstance(context:Context):SurahRepository{
            return SurahRepository(PersistenceDatabase.getDatabase(context).surahDao())
        }
    }

    suspend fun getSurahByUrl(url:String):Surah?{
        return dao.getSurahById(url.getPath1FromUrl()).firstOrNull()
    }

    suspend fun getSurahById(id:Int):Surah?{
        return dao.getSurahById(id).firstOrNull()
    }
}