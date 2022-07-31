package com.wagyufari.dzikirqu.ui.khatam.composer

import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseNavigator
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.Khatam
import com.wagyufari.dzikirqu.model.KhatamStateConstants
import com.wagyufari.dzikirqu.model.QuranLastRead
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class KhatamComposerViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {}

    val khatam:MutableLiveData<Khatam> = MutableLiveData()

    fun addNewKhatam(){
        khatam.value = khatam.value?.apply {
            iteration = iteration?.toCollection(arrayListOf())?.apply {
                add(QuranLastRead(
                    surah = 1,
                    ayah = 1,
                    page = null,
                    isSavedFromPage = null,
                    timestamp = null
                ).apply {
                    lap = iteration?.count()?.plus(1) ?: 1
                    state = KhatamStateConstants.INACTIVE
                })
            }
        }
    }

}