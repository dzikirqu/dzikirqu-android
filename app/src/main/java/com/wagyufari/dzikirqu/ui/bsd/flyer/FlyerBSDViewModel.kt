package com.wagyufari.dzikirqu.ui.bsd.flyer

import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel

import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.Flyer
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