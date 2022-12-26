package com.dzikirqu.android.ui.bsd.flyer

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel

import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.Flyer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlyerBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<BaseNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val flyer = MutableLiveData<Flyer>()

}