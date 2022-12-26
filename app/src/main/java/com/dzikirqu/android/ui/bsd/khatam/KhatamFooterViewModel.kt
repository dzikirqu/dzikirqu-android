package com.dzikirqu.android.ui.bsd.khatam

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KhatamFooterViewModel @Inject constructor(
    dataManager: AppDataManager,
) :
    BaseViewModel<KhatamFooterNavigator>(dataManager) {

    val textKhatamIndex = MutableLiveData("")
    val textKhatamProgress = MutableLiveData("")
    val textKhatamProgressInt = MutableLiveData(0)
    val textKhatamProgressPercentage = MutableLiveData("")
    val textKhatamLastRead = MutableLiveData("")

    val readCurrentAyahInPagedMode = MutableLiveData("Read Al-Fatihah 4 in paged mode")

    fun onClickPage() {
        navigator?.onClickPage()
    }

    override fun onEvent(obj: Any) {
    }

}