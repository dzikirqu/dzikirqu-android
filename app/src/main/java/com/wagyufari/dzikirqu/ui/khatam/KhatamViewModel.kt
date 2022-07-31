package com.wagyufari.dzikirqu.ui.khatam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.model.Khatam
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class KhatamViewModel @Inject constructor(
    dataManager: AppDataManager,
) :
    BaseViewModel<KhatamNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }

    val currentKhatam: MutableLiveData<Khatam?> = MutableLiveData()

    val hijriMonth = currentKhatam.switchMap { khatam ->
        liveData {
            emit(UmmalquraCalendar().apply { khatam?.startDate?.let { time = it } }.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            ))
        }
    }
    val hijriYear = currentKhatam.switchMap { khatam ->
        liveData {
            emit(UmmalquraCalendar().apply { khatam?.startDate?.let { time = it } }
                .get(Calendar.YEAR))
        }
    }

    fun getProgress(khatam: Khatam) {
        val maxValue = 604 * (khatam.iteration?.count() ?: 1)
    }

    fun getMonthString(khatam: Khatam): String {
        val calendar = UmmalquraCalendar().apply {
            khatam.startDate?.let {
                time = it
            }
        }
        return "${
            calendar.getDisplayName(Calendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH)
        } ${calendar.get(Calendar.YEAR)}"
    }

}

enum class KhatamCalculationMethod {
    PAGE,
    AYAH
}

fun KhatamCalculationMethod.getWithName(): String {
    return when (this) {
        KhatamCalculationMethod.PAGE -> LocaleConstants.BY.locale() + " " + LocaleConstants.PAGE.locale()
        KhatamCalculationMethod.AYAH -> LocaleConstants.BY.locale() + " " + LocaleConstants.AYAH.locale()
    }
}