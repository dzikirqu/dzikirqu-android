//package com.wagyufari.dzikirqu.ui.v2.main.home
//
//
//import android.os.CountDownTimer
//import android.util.Log
//import androidx.compose.runtime.mutableStateOf
//import androidx.databinding.ObservableField
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.liveData
//import androidx.lifecycle.viewModelScope
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.wagyufari.dzikirqu.base.BaseViewModel
//import com.wagyufari.dzikirqu.constants.LocaleConstants
//import com.wagyufari.dzikirqu.data.AppDataManager
//import com.wagyufari.dzikirqu.data.Prefs
//import com.wagyufari.dzikirqu.data.room.dao.AyahDao
//import com.wagyufari.dzikirqu.data.room.dao.SurahDao
//import com.wagyufari.dzikirqu.model.Ayah
//import com.wagyufari.dzikirqu.model.Flyer
//import com.wagyufari.dzikirqu.model.Surah
//import com.wagyufari.dzikirqu.model.events.MenuEvent
//import com.wagyufari.dzikirqu.ui.main.home.MainHomeNavigator
//import com.wagyufari.dzikirqu.util.BooleanUtil.isLocationGranted
//import com.wagyufari.dzikirqu.util.BooleanUtil.isUserCoordinatesNull
//import com.wagyufari.dzikirqu.util.FileUtils
//import com.wagyufari.dzikirqu.util.HadithUtils
//import com.wagyufari.dzikirqu.util.LocaleProvider
//import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper
//import com.wagyufari.dzikirqu.util.tick
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class MainHomeV2ViewModel @Inject constructor(
//    dataManager: AppDataManager,
//    val ayahDao: AyahDao,
//    val surahDao: SurahDao
//) : BaseViewModel<MainHomeNavigator>(dataManager) {
//
//    override fun onEvent(obj: Any) {
//        when (obj) {
//            is MenuEvent -> {
//                setUpLastRead()
//            }
//        }
//    }
//
//    val praytime = mutableStateOf(Prefs.praytime)
//    val address = mutableStateOf(Prefs.userCity)
//
//    init {
//        viewModelScope.launch {
//            PrayerTimeHelper.getPrayerTime(dataManager.mContext)
//        }
//    }
//
//    val hadith = liveData(IO) { emit(HadithUtils.getHadiths()) }
//    val highlights = dataManager.mBookmarkDatabase.bookmarkDao().getHighlights()
//    val flyers: MutableLiveData<List<Flyer>> = MutableLiveData(listOf())
//    val lastReadSurah = mutableStateOf(LocaleProvider.getString(LocaleConstants.NOT_SET))
//    val lastReadAyah =
//        mutableStateOf(String.format(LocaleProvider.getString(LocaleConstants.AYAH_N), 0))
//
//    var newtimer: CountDownTimer? = null
//    var textPrayerTime = ObservableField("Loading...")
//    var textUntil = ObservableField("Loading...")
//
//    var isPrayerTimeNeedsPermission = MutableLiveData(false)
//    var isPrayerTimeHidden = MutableLiveData(false)
//    var isPrayerTimeLoading = MutableLiveData(false)
//
//    fun setUpLastRead() {
//        viewModelScope.launch(IO){
//            val lastRead = Prefs.quranLastRead
//            surahDao.getSurahById(lastRead.surah).firstOrNull()?.let {
//                lastReadSurah.value = it.name
//                lastReadAyah.value = String.format(LocaleProvider.getString(LocaleConstants.AYAH_N), lastRead.ayah)
//            }
//        }
//    }
//
//    fun onClickLastRead() {
//        viewModelScope.launch(IO) {
//            if (ayahDao.getAyahCount() != 6236 && surahDao.getSurah().isEmpty()) {
//                navigator?.showLoading()
//                getQuranData {
//                    navigator?.onClickLastRead()
//                    navigator?.hideLoading()
//                }
//            } else {
//                navigator?.onClickLastRead()
//                navigator?.hideLoading()
//            }
//        }
//    }
//
//    fun onLongClickLastRead(): Boolean {
//        viewModelScope.launch(IO) {
//            if (ayahDao.getAyahCount() != 6236 && surahDao.getSurah().isEmpty()) {
//                navigator?.showLoading()
//                getQuranData {
//                    navigator?.onLongClickLastRead()
//                    navigator?.hideLoading()
//                }
//            } else {
//                navigator?.onLongClickLastRead()
//                navigator?.hideLoading()
//            }
//        }
//        return true
//    }
//
//    fun buildPrayerTime() {
//        newtimer?.cancel()
//        newtimer = tick(Long.MAX_VALUE, 1000) {
//            isPrayerTimeNeedsPermission.value = !isLocationGranted()
//            isPrayerTimeHidden.value = isUserCoordinatesNull()
//            isPrayerTimeLoading.value = isUserCoordinatesNull() && isLocationGranted()
//            praytime.value = Prefs.praytime
////            textPrayerTime.set(Prefs.praytime.getCurrentPrayerTimeString())
////            textUntil.set(Prefs.praytime.getTimeUntilNextPrayerString())
//            address.value = Prefs.userCity
//        }
//        newtimer?.start()
//    }
//
//    suspend fun getQuranData(onDone: () -> Unit) {
//        try {
//            val surah = Gson().fromJson<ArrayList<Surah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/surah.json"
//                ) {},
//                object : TypeToken<ArrayList<Surah>>() {}.type,
//            )
//            surahDao.putSurah(surah)
//            ayahDao.deleteAyah()
//            val juz1_5 = Gson().fromJson<ArrayList<Ayah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/quran1.json"
//                ) {},
//                object : TypeToken<ArrayList<Ayah>>() {}.type,
//            )
//            ayahDao.putAyah(juz1_5)
//            val juz6_10 = Gson().fromJson<ArrayList<Ayah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/quran2.json"
//                ) {},
//                object : TypeToken<ArrayList<Ayah>>() {}.type,
//            )
//            ayahDao.putAyah(juz6_10)
//            val juz11_15 = Gson().fromJson<ArrayList<Ayah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/quran3.json"
//                ) {},
//                object : TypeToken<ArrayList<Ayah>>() {}.type,
//            )
//            ayahDao.putAyah(juz11_15)
//            val juz16_20 = Gson().fromJson<ArrayList<Ayah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/quran4.json"
//                ) {},
//                object : TypeToken<ArrayList<Ayah>>() {}.type,
//            )
//            ayahDao.putAyah(juz16_20)
//            val juz21_25 = Gson().fromJson<ArrayList<Ayah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/quran5.json"
//                ) {},
//                object : TypeToken<ArrayList<Ayah>>() {}.type,
//            )
//            ayahDao.putAyah(juz21_25)
//            val juz26_30 = Gson().fromJson<ArrayList<Ayah>>(
//                FileUtils.getJsonStringFromAssets(
//                    dataManager.mContext,
//                    "json/quran/quran6.json"
//                ) {},
//                object : TypeToken<ArrayList<Ayah>>() {}.type,
//            )
//            ayahDao.putAyah(juz26_30)
//            onDone.invoke()
//        } catch (e: java.lang.Exception) {
//            Log.wtf("Quran Import", e.message)
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        if (newtimer != null) {
//            (newtimer as CountDownTimer).cancel()
//        }
//    }
//}