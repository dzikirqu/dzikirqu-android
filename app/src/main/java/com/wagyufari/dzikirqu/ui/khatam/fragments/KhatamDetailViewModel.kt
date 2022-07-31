package com.wagyufari.dzikirqu.ui.khatam.fragments

import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KhatamDetailViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<KhatamDetailNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }
}