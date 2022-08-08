package com.wagyufari.dzikirqu.ui.read.prayer

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.PrayerData
import com.wagyufari.dzikirqu.util.StringExt.getText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadPrayerViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<ReadPrayerNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    val prayerId = MutableLiveData<String>()
    val bookName = mutableStateOf("")
    val prayerName = mutableStateOf("")
    val recite = MutableLiveData<String>()
    val position = MutableLiveData(0)

    val counter = mutableStateMapOf<String, Int>()

    val prayerData = prayerId.switchMap {
        dataManager.getPrayerData(it)
    }

    fun onSuccessPrayerData(data:List<PrayerData>){
        viewModelScope.launch(Dispatchers.IO){
            bookName.value = getBookNameFromId(data.firstOrNull()?.book_id.toString())
            prayerName.value = getPrayerNameFromId(data.firstOrNull()?.prayer_id.toString())
        }
    }

    suspend fun getBookNameFromId(bookId:String):String{
        return dataManager.getBookById(bookId)?.title?.getText().toString()
    }

    suspend fun getPrayerNameFromId(prayerId:String):String{
        return dataManager.getPrayerById(prayerId)?.title?.getText().toString()
    }

    fun onClickSettings(){
        navigator?.onClickSettings()
    }

}