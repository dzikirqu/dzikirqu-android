package com.wagyufari.dzikirqu.ui.jump

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.room.dao.SurahDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JumpQuranViewModel @Inject constructor(dataManager: AppDataManager, surahDao: SurahDao) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }

    val query = MutableLiveData<String>()
    val surah = query.switchMap { query->
        liveData {
            if (isFirstValueNumber()){
                emit(surahDao.getSurah().filter { it.id ==  getFirstNumber()})
            } else{
                emit(surahDao.getSurah().filter { it.name.filter(Char::isLetter).lowercase().contains(query.split(" ")[0].lowercase()) })
            }
        }
    }


    fun isFirstValueNumber():Boolean{
        return query.value?.split(" ")?.get(0)?.filter(Char::isLetter).isNullOrEmpty().not() && query.value?.split(" ")?.get(0)?.filter(Char::isLetter).isNullOrEmpty()
    }

    fun isHasSecondValue():Boolean{
        return query.value?.split(" ")?.count() ?: 0 > 1
    }

    fun isSecondValueNumber():Boolean{
        return query.value?.split(" ")?.get(1)?.filter(Char::isDigit).isNullOrEmpty().not()  && query.value?.split(" ")?.get(1)?.filter(Char::isLetter).isNullOrEmpty()
    }

    fun getSecondNumber():Int{
        return query.value?.split(" ")?.get(1)?.filter(Char::isDigit).toString().toInt()
    }
    fun getFirstNumber():Int{
        return query.value?.split(" ")?.get(0)?.filter(Char::isDigit).toString().toInt()
    }

}