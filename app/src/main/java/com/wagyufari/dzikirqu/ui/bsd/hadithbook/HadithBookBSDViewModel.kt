package com.wagyufari.dzikirqu.ui.bsd.hadithbook

import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HadithBookBSDViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<HadithBookBSDNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }
}