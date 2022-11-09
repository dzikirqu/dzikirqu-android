package com.dzikirqu.android.ui.khatam.fragments

import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
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