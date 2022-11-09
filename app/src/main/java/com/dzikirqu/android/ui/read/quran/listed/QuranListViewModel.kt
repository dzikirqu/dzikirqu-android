package com.dzikirqu.android.ui.read.quran.listed


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.room.dao.AyahDao
import com.dzikirqu.android.data.room.dao.SurahDao
import com.dzikirqu.android.model.Ayah
import com.dzikirqu.android.model.AyahWbw
import com.dzikirqu.android.model.identifiers.BismillahObject
import com.dzikirqu.android.model.identifiers.JuzObject
import com.dzikirqu.android.model.identifiers.SurahObject
import com.dzikirqu.android.ui.read.quran.AyahQuery
import com.dzikirqu.android.ui.read.quran.AyahQueryType
import com.dzikirqu.android.util.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuranListViewModel @Inject constructor(
    dataManager: AppDataManager,
    val ayahDao: AyahDao,
    val surahDao: SurahDao
) : BaseViewModel<BaseNavigator>(dataManager) {


    suspend fun getSurah(surahId:Int):List<Ayah> {
        return ayahDao.getAyahBySurahId(surahId)
    }

    fun getAyah(query: AyahQuery):LiveData<ArrayList<Any>>{
        return liveData {
            emit(getAyahData(query))
        }
    }

    private suspend fun getAyahData(query: AyahQuery): ArrayList<Any> {
        val wbw = Gson().fromJson<ArrayList<AyahWbw>>(
            FileUtils.getJsonStringFromAssets(
                dataManager.mContext,
                "json/quran/wbw/${query.id.toInt()}.json"
            ), object : TypeToken<ArrayList<AyahWbw>>() {}.type
        )
        val data = when (query.type) {
            AyahQueryType.Surah -> ayahDao.getAyahBySurahId(query.id.toInt())
            AyahQueryType.Juz -> ayahDao.getAyahByJuz(query.id.toInt(), 9999)
            AyahQueryType.Hizb -> ayahDao.getAyahByHizb(query.id, 9999)
        }
        return arrayListOf<Any>().apply {
            var currentJuz = -1
            data.forEach {
                if (currentJuz != it.juz){
                    currentJuz = it.juz
                    if (dataManager.isFirstAyahOfJuz(it.juz, it.chapterId, it.verse_number)){
                        add(JuzObject(it.juz))
                    }
                }
                if (it.verse_number == 1) {
                    add(SurahObject(surahDao.getSurahById(it.chapterId).first()))
                }
                if (it.use_bismillah == true) {
                    add(BismillahObject())
                }
                add(it.apply {
                    this.wbw.addAll(wbw.filter { it.ayah == this.verse_number })
                })
            }
        }
    }

    override fun onEvent(obj: Any) {
    }

}