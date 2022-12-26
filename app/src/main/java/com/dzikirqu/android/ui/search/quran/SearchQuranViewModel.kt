package com.dzikirqu.android.ui.search.quran

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.events.SearchEvent
import com.dzikirqu.android.model.search.SearchQuranModel
import com.dzikirqu.android.util.StringExt.getText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchQuranViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is SearchEvent->{
                query.value = obj.query
            }
        }
    }

    val query = MutableLiveData("")
    val result = query.switchMap { query->
        liveData<List<SearchQuranModel>>{
            if (query.isNullOrBlank().not()){
                val result = dataManager.mPersistenceDatabase.ayahDao().searchAyah("%$query%").filter {
                    it.text.getText().lowercase().contains(query.lowercase())
                }
                val chapterId = result.map {
                    it.chapterId
                }.toSet()
                emit(chapterId.map { id->
                    SearchQuranModel(id, result.filter { id == it.chapterId })
                })
            } else{
                emit(arrayListOf())
            }
        }
    }

}