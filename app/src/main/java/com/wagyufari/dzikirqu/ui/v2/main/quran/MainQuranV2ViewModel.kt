//package com.wagyufari.dzikirqu.ui.v2.main.quran
//
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.viewModelScope
//import com.wagyufari.dzikirqu.base.BaseViewModel
//import com.wagyufari.dzikirqu.data.AppDataManager
//import com.wagyufari.dzikirqu.data.Prefs
//import com.wagyufari.dzikirqu.data.room.dao.AyahDao
//import com.wagyufari.dzikirqu.data.room.dao.SurahDao
//import com.wagyufari.dzikirqu.model.Surah
//import com.wagyufari.dzikirqu.ui.main.home.MainHomeNavigator
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MainQuranV2ViewModel @Inject constructor(
//    dataManager: AppDataManager,
//    val ayahDao: AyahDao,
//    val surahDao: SurahDao
//) : BaseViewModel<MainHomeNavigator>(dataManager) {
//
//    override fun onEvent(obj: Any) {
//    }
//
//    val praytime = mutableStateOf(Prefs.praytime)
//    val address = mutableStateOf(Prefs.userCity)
//    var surah = mutableStateOf<List<Surah>>(listOf())
//
//    init{
//        viewModelScope.launch {
//            surah.value = dataManager.mPersistenceDatabase.surahDao().getSurah()
//        }
//    }
//
//}