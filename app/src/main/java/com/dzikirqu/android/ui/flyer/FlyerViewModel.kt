package com.dzikirqu.android.ui.flyer

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
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