package com.wagyufari.dzikirqu.ui.bsd.khatam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.events.CurrentAyahEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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