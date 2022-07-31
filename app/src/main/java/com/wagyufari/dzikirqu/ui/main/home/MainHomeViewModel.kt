package com.wagyufari.dzikirqu.ui.main.home


import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.AyahDao
import com.wagyufari.dzikirqu.data.room.dao.SurahDao
import com.wagyufari.dzikirqu.data.room.dao.getDailyReminderDao
import com.wagyufari.dzikirqu.model.Ayah
import com.wagyufari.dzikirqu.model.Flyer
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.model.events.ApplyWindowEvent
import com.wagyufari.dzikirqu.model.events.MenuEvent
import com.wagyufari.dzikirqu.util.*
import com.wagyufari.dzikirqu.util.BooleanUtil.isLocationGranted
import com.wagyufari.dzikirqu.util.BooleanUtil.isUserCoordinatesNull
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getCurrentPrayerTimeString
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getTimeUntilNextPrayerString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainHomeViewModel @Inject constructor(
    dataManager: AppDataManager,
    val ayahDao: AyahDao,
    val surahDao: SurahDao,
) :
    BaseViewModel<MainHomeNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
        when(obj){
            is MenuEvent -> {
                setUpLastRead()
            }
            is ApplyWindowEvent->{
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }

    val prayerTime = MutableLiveData(Prefs.praytime)
    val address = MutableLiveData(Prefs.userCity)
    val time = MutableLiveData(Calendar.getInstance().time)

    init {
        viewModelScope.launch {
            PrayerTimeHelper.getPrayerTime(dataManager.mContext)
        }
    }

    val hadith = liveData(IO){ emit(HadithUtils.getHadiths()) }
    val highlights = dataManager.mBookmarkDatabase.bookmarkDao().getHighlights()
    val flyers: MutableLiveData<List<Flyer>> = MutableLiveData(listOf())
    val lastReadSurah = MutableLiveData(LocaleProvider.getString(LocaleConstants.NOT_SET))
    val lastReadAyah =
        MutableLiveData(String.format(LocaleProvider.getString(LocaleConstants.AYAH_N), 0))

    var newtimer: CountDownTimer? = null
    var newMinuteTimer: CountDownTimer? = null
    var textPrayerTime = MutableLiveData("Loading...")
    var textUntil = MutableLiveData("Loading...")

    val dailyReminder = dataManager.mContext.getDailyReminderDao().getDailyReminder()

    var isPrayerTimeNeedsPermission = MutableLiveData(false)
    var isPrayerTimeHidden = MutableLiveData(false)
    var isPrayerTimeLoading = MutableLiveData(false)

    val statusBarHeight = MutableLiveData(0)

    fun setUpLastRead() {
        CoroutineScope(IO).launch {
            val lastRead = Prefs.quranLastRead
            surahDao.getSurahById(lastRead.surah).firstOrNull()?.let {
                lastReadSurah.postValue(it.name)
                lastReadAyah.postValue(
                    String.format(LocaleProvider.getString(LocaleConstants.AYAH_N), lastRead.ayah)
                )
            }
        }
    }

    fun buildPrayerTime() {
        newtimer?.cancel()
        newtimer = tick(Long.MAX_VALUE, 1000) {
            if (Calendar.getInstance().get(Calendar.MINUTE) != Calendar.getInstance().apply { time = this@MainHomeViewModel.time.value!! }.get(Calendar.MINUTE)){
                time.value = Calendar.getInstance().apply {
                    add(Calendar.MINUTE, 1)
                }.time
            }
            isPrayerTimeNeedsPermission.value = !isLocationGranted()
            isPrayerTimeHidden.value = isUserCoordinatesNull()
            isPrayerTimeLoading.value = isUserCoordinatesNull() && isLocationGranted()
            textPrayerTime.value = Prefs.praytime.getCurrentPrayerTimeString()
            textUntil.value = Prefs.praytime.getTimeUntilNextPrayerString()
            address.value = Prefs.userCity
            PraytimeWidget.update(dataManager.mContext)
        }
        newtimer?.start()
    }

    suspend fun getQuranData(onDone: () -> Unit) {
        try {
            val surah = Gson().fromJson<ArrayList<Surah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/surah.json"
                ) {}, object : TypeToken<ArrayList<Surah>>() {}.type,
            )
            surahDao.putSurah(surah)
            ayahDao.deleteAyah()
            val juz1_5 = Gson().fromJson<ArrayList<Ayah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/quran1.json"
                ) {},
                object : TypeToken<ArrayList<Ayah>>() {}.type,
            )
            ayahDao.putAyah(juz1_5)
            val juz6_10 = Gson().fromJson<ArrayList<Ayah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/quran2.json"
                ) {},
                object : TypeToken<ArrayList<Ayah>>() {}.type,
            )
            ayahDao.putAyah(juz6_10)
            val juz11_15 = Gson().fromJson<ArrayList<Ayah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/quran3.json"
                ) {},
                object : TypeToken<ArrayList<Ayah>>() {}.type,
            )
            ayahDao.putAyah(juz11_15)
            val juz16_20 = Gson().fromJson<ArrayList<Ayah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/quran4.json"
                ) {},
                object : TypeToken<ArrayList<Ayah>>() {}.type,
            )
            ayahDao.putAyah(juz16_20)
            val juz21_25 = Gson().fromJson<ArrayList<Ayah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/quran5.json"
                ) {},
                object : TypeToken<ArrayList<Ayah>>() {}.type,
            )
            ayahDao.putAyah(juz21_25)
            val juz26_30 = Gson().fromJson<ArrayList<Ayah>>(
                FileUtils.getJsonStringFromAssets(
                    dataManager.mContext,
                    "json/quran/quran6.json"
                ) {},
                object : TypeToken<ArrayList<Ayah>>() {}.type,
            )
            ayahDao.putAyah(juz26_30)
            onDone.invoke()
        } catch (e: java.lang.Exception) {
            Log.wtf("Quran Import", e.message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (newtimer != null) {
            (newtimer as CountDownTimer).cancel()
        }
    }
}