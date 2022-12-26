package com.dzikirqu.android.ui.search.prayer


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.Prayer
import com.dzikirqu.android.model.events.SearchEvent
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