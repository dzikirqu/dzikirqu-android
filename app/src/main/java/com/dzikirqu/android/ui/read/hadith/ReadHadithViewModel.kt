package com.dzikirqu.android.ui.read.hadith

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.room.dao.HadithDataDao
import com.dzikirqu.android.model.events.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

@HiltViewModel
class ReadHadithViewModel @Inject constructor(
    dataManager: AppDataManager,
    hadithDataDao: HadithDataDao
) :
    BaseViewModel<ReadHadithNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        if (obj is SettingsEvent) {
            navigator?.reloadViews()
        }
    }

    val startIndex: MutableLiveData<Int?> = MutableLiveData(0)
    val endIndex: MutableLiveData<Int?> = MutableLiveData(-1)
    val hadithId = MutableLiveData("")
    val hadith = hadithId.switchMap {
        liveData(IO) {
            if (it.isNotBlank()) {
                if (startIndex.value != 0 && endIndex.value != 0) {
                    emit(
                        hadithDataDao.getHadithById(it)
                            .filter { it.index.toInt() >= startIndex.value ?: 0 }.filter {
                            it.index.toInt() <= endIndex.value ?: 0
                        })
                } else {
                    emit(hadithDataDao.getHadithById(it))
                }
            }
        }
    }

}