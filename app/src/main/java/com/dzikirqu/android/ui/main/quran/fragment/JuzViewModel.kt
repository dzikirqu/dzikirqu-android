package com.dzikirqu.android.ui.main.quran.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.room.dao.SurahDao
import com.dzikirqu.android.data.room.dao.AyahDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

@HiltViewModel
class JuzViewModel @Inject constructor(
    dataManager: AppDataManager,
    val surahDao: SurahDao,
    val ayahDao: AyahDao
) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }


    val isLoading = MutableLiveData(true)
    val juzLiveData = MutableLiveData<List<Any>>()
    val isShowHizbOnJuz = MutableLiveData(0)

    val juz = isShowHizbOnJuz.switchMap {
        liveData(IO){
            if (isShowHizbOnJuz.value!! > 0){
                emit(dataManager.getJuzWithHizb(it))
            } else{
                emit(dataManager.getJuz())
            }
        }
    }

//
//    suspend fun getHizb():ArrayList<Hizb>{
//        return arrayListOf<Hizb>().apply {
//            for (i in 1..60){
//                add(getHizb(i.toFloat()))
//                add(getHizb(i.toFloat()+.25f))
//                add(getHizb(i.toFloat()+.50f))
//                add(getHizb(i.toFloat()+.75f))
//            }
//        }
//    }
//
//    suspend fun getHizb(hizb:Float):Hizb{
//        val ayah = ayahDao.getAyahByHizb(hizb, 1).firstOrNull()
//        val surah = surahDao.getSurahById(ayah?.chapterId?:0).firstOrNull()
//        return Hizb(hizb = hizb, surah = surah?.name?:"", verse = ayah?.verse_number?:0, juz = ayah?.juz?:0)
//    }
}