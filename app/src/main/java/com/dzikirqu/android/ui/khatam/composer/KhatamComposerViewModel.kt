package com.dzikirqu.android.ui.khatam.composer

import androidx.lifecycle.MutableLiveData
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.Khatam
import com.dzikirqu.android.model.KhatamStateConstants
import com.dzikirqu.android.model.QuranLastRead
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