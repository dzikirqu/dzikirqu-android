package com.wagyufari.dzikirqu.ui.praytime

import android.os.CountDownTimer
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.model.PrayerTime
import com.wagyufari.dzikirqu.util.LocaleProvider
import com.wagyufari.dzikirqu.util.getHijriDate
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getCurrentPrayerTimeString
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getTimeUntilNextPrayerString
import com.wagyufari.dzikirqu.util.tick
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PraytimeViewModel @Inject constructor(
    dataManager: AppDataManager
) :
    BaseViewModel<PraytimeNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
    }

    var prayerTime = MutableLiveData(Prefs.praytime)

    var newtimer: CountDownTimer? = null

    val currentTime = ObservableField("")
    val address = ObservableField(Prefs.userCity)
    val hijriDate = ObservableField(getHijriDate())
    val gregoryDate = ObservableField(dataManager.getGregorianDate())
    val dates = ObservableField("${dataManager.getGregorianDate()}/${dataManager.getHijriDate()}")

    val isDeviceHasCompass = ObservableField(false)

    var textPrayerTime = ObservableField("Loading...")
    var textUntil = ObservableField("Loading...")

    var untilFajr = MutableLiveData("")
    var untilDhuhr = MutableLiveData("")
    var untilAsr = MutableLiveData("")
    var untilMaghrib = MutableLiveData("")
    var untilIsya = MutableLiveData("")

    var praytimeFajr = MutableLiveData("")
    var praytimeDhuhr = MutableLiveData("")
    var praytimeAsr = MutableLiveData("")
    var praytimeMaghrib = MutableLiveData("")
    var praytimeIsya = MutableLiveData("")

    var ringFajr = MutableLiveData(Prefs.ringFajr)
    var ringDhuhr = MutableLiveData(Prefs.ringDhuhr)
    var ringAsr = MutableLiveData(Prefs.ringAsr)
    var ringMaghrib = MutableLiveData(Prefs.ringMaghrib)
    var ringIsya = MutableLiveData(Prefs.ringIsya)

    fun buildPrayers(prayer: PrayerTime) {
        untilFajr.value =
            PrayerTimeHelper.countTimeLight(
                prayer.fajr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.FAJR)
            )
        praytimeFajr.value = prayer.fajr

        untilDhuhr.value =
            PrayerTimeHelper.countTimeLight(
                prayer.dhuhr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.DHUHR)
            )
        praytimeDhuhr.value = prayer.dhuhr

        untilAsr.value =
            PrayerTimeHelper.countTimeLight(
                prayer.asr ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.ASR)
            )
        praytimeAsr.value = prayer.asr

        untilMaghrib.value =
            PrayerTimeHelper.countTimeLight(
                prayer.maghrib ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.MAGHRIB)
            )
        praytimeMaghrib.value = prayer.maghrib

        untilIsya.value =
            PrayerTimeHelper.countTimeLight(
                prayer.isya ?: "",
                LocaleProvider.getInstance().getString(LocaleConstants.ISYA)
            )
        praytimeIsya.value = prayer.isya

    }

    fun onClickQibla(){
        navigator?.onClickQibla()
    }

    fun configureTicker() {
        newtimer?.cancel()
        newtimer = tick(Long.MAX_VALUE, 1000) {
            buildPrayers(Prefs.praytime)
            currentTime.set(SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time))
            textPrayerTime.set(Prefs.praytime.getCurrentPrayerTimeString())
            textUntil.set(Prefs.praytime.getTimeUntilNextPrayerString())
        }
        newtimer?.start()
    }

}