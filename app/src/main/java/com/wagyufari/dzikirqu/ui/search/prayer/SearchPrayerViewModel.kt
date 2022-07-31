package com.wagyufari.dzikirqu.ui.search.prayer


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.model.events.SearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchPrayerViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SearchEvent ->{
                query.value = obj.query
                isNoteDeeplink = obj.isNoteDeeplinkEvent == true
            }
        }
    }

    var isNoteDeeplink = false
    val query = MutableLiveData("")
    val result = query.switchMap { query->
        liveData<List<Prayer>>{
            if (query.isNullOrBlank().not()){
                val result = dataManager.mPersistenceDatabase.prayerDao().searchPrayer("%$query%")
                emit(result)
            } else{
                emit(arrayListOf())
            }
        }
    }


}