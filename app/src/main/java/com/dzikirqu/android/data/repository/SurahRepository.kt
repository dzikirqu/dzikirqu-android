package com.dzikirqu.android.data.repository

import android.content.Context
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.data.room.dao.SurahDao
import com.dzikirqu.android.model.Surah
import com.dzikirqu.android.util.getPath1FromUrl
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