package com.wagyufari.dzikirqu.ui.onboarding.fragments.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.room.dao.*
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.util.FileUtils
import com.wagyufari.dzikirqu.util.ResultEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardLoadingViewModel @Inject constructor(
    dataManager: AppDataManager,
    val bookDao: PrayerBookDao,
    val ayahDao: AyahDao,
    val ayahLineDao: AyahLineDao,
    val surahDao: SurahDao,
    val hadithDataDao: HadithDataDao,
    val prayerDao: PrayerDao,
    val prayerDataDao: PrayerDataDao
) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {

    }

    val isBookLoaded= MutableLiveData(ResultEnum.Loading)
    val isPrayerLoaded= MutableLiveData(ResultEnum.Loading)
    val isQuranLoaded= MutableLiveData(ResultEnum.Loading)
    val isHadithLoaded= MutableLiveData(ResultEnum.Loading)
    val mQuranImportProgress = MutableLiveData(0)
    val mHadithImportProgress = MutableLiveData(0)

    val page = MutableLiveData(0)

    init {
        viewModelScope.launch(Dispatchers.IO){
            getBooks()
        }
    }

    fun refreshBooks(){
        viewModelScope.launch(Dispatchers.IO){
            isBookLoaded.postValue(ResultEnum.Loading)
            getBooks()
        }
    }

    fun refreshPrayer(){
        viewModelScope.launch(Dispatchers.IO){
            isPrayerLoaded.postValue(ResultEnum.Loading)
            getPrayer()
        }
    }

    suspend fun getBooks(){
        try {
            bookDao.putBooks(Gson().fromJson<ArrayList<PrayerBook>>(
                FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/prayer/book.json"),
                object : TypeToken<ArrayList<PrayerBook>>() {}.type))
            isBookLoaded.postValue(ResultEnum.Success)
            getPrayer()
        } catch (e:Exception){
            isBookLoaded.postValue(ResultEnum.Error)
        }
    }
    suspend fun getPrayer(){
        try {
            prayerDao.putPrayer(Gson().fromJson<ArrayList<Prayer>>(
                FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/prayer/prayer.json"),
                object : TypeToken<ArrayList<Prayer>>() {}.type))
            getPrayerData()
        }catch (e:Exception){
            isPrayerLoaded.postValue(ResultEnum.Error)
        }
    }
    suspend fun getPrayerData(){
        try {
            prayerDataDao.putPrayerData(Gson().fromJson<ArrayList<PrayerData>>(
                FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/prayer/prayerData.json"),
                object : TypeToken<ArrayList<PrayerData>>() {}.type))
            isPrayerLoaded.postValue(ResultEnum.Success)
            getQuranData()
        }catch (e:Exception){
            isPrayerLoaded.postValue(ResultEnum.Error)
        }
    }

    suspend fun getHadithData(){
        try{
            hadithDataDao.deleteHadithData()
            putHadith("json/hadith/nawawi40.json")
            mHadithImportProgress.postValue(5)
            for (i in 0..19){
                putHadith("json/hadith/riyadussalihin$i.json")
                mHadithImportProgress.postValue(mHadithImportProgress.value?.plus(5) ?: 5)
            }
            isHadithLoaded.postValue(ResultEnum.Success)
        } catch (e:java.lang.Exception){
            Log.wtf("Hadith Import",e.message)
        }
    }

    suspend fun putHadith(path:String){
        hadithDataDao.putHadithData(Gson().fromJson<ArrayList<HadithData>>(
            FileUtils.getJsonStringFromAssets(dataManager.mContext, path),
            object : TypeToken<ArrayList<HadithData>>() {}.type))
    }

    suspend fun getQuranData(){
        try{
            ayahDao.deleteAyah()
            ayahLineDao.delete()

            for (i in 1..604) {
                ayahLineDao.putAyahLine(
                    Gson().fromJson<ArrayList<AyahLine>>(
                        FileUtils.getJsonStringFromAssets(
                            dataManager.mContext,
                            "json/quran/paged/page$i.json"
                        ) {},
                        object : TypeToken<ArrayList<AyahLine>>() {}.type,
                    ).map {
                        it.apply {
                            this.page = i
                        }
                    })
                mQuranImportProgress.postValue(i)
            }

            val surah = Gson().fromJson<ArrayList<Surah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/surah.json") {}, object : TypeToken<ArrayList<Surah>>() {}.type,)
            surahDao.putSurah(surah)
            mQuranImportProgress.postValue(605)
            val juz1_5 = Gson().fromJson<ArrayList<Ayah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/quran1.json") {}, object : TypeToken<ArrayList<Ayah>>() {}.type,)
            ayahDao.putAyah(juz1_5)
            mQuranImportProgress.postValue(606)
            val juz6_10 = Gson().fromJson<ArrayList<Ayah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/quran2.json") {}, object : TypeToken<ArrayList<Ayah>>() {}.type,)
            ayahDao.putAyah(juz6_10)
            mQuranImportProgress.postValue(607)
            val juz11_15 = Gson().fromJson<ArrayList<Ayah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/quran3.json") {}, object : TypeToken<ArrayList<Ayah>>() {}.type,)
            ayahDao.putAyah(juz11_15)
            mQuranImportProgress.postValue(608)
            val juz16_20 = Gson().fromJson<ArrayList<Ayah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/quran4.json") {}, object : TypeToken<ArrayList<Ayah>>() {}.type,)
            ayahDao.putAyah(juz16_20)
            mQuranImportProgress.postValue(609)
            val juz21_25 = Gson().fromJson<ArrayList<Ayah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/quran5.json") {}, object : TypeToken<ArrayList<Ayah>>() {}.type,)
            ayahDao.putAyah(juz21_25)
            mQuranImportProgress.postValue(610)
            val juz26_30 = Gson().fromJson<ArrayList<Ayah>>(FileUtils.getJsonStringFromAssets(dataManager.mContext, "json/quran/quran6.json") {}, object : TypeToken<ArrayList<Ayah>>() {}.type,)
            ayahDao.putAyah(juz26_30)
            mQuranImportProgress.postValue(611)
            if (ayahDao.getAyahCount() != 6236){
                getQuranData()
                return
            }
            isQuranLoaded.postValue(ResultEnum.Success)
            getHadithData()
        } catch (e:java.lang.Exception){
            Log.wtf("Quran Import",e.message)
            isQuranLoaded.postValue(ResultEnum.Success)
        }
    }

}