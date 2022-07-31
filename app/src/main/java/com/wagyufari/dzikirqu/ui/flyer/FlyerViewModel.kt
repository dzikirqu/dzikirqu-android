package com.wagyufari.dzikirqu.ui.flyer

import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlyerViewModel @Inject constructor(dataManager: AppDataManager): BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {

    }

    val grid = MutableLiveData(1)

//    val flyer = Pager(config = PagingConfig(pageSize = 9, prefetchDistance = 2),
//        pagingSourceFactory = { FlyerPagingDataSource(dataManager.mApiService, dataManager.mContext) }
//    ).flow.cachedIn(viewModelScope)

    fun onClickGrid1(){
        grid.value = 1
    }
    fun onClickGrid2(){
        grid.value = 2
    }
    fun onClickGrid3(){
        grid.value = 3
    }

}