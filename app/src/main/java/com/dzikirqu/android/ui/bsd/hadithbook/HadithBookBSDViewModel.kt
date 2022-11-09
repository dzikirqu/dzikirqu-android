package com.dzikirqu.android.ui.bsd.hadithbook

import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
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