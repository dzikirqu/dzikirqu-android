package com.wagyufari.dzikirqu.model

import android.app.AlarmManager
import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getAyahLineDao
import com.wagyufari.dzikirqu.data.room.dao.getKhatamDao
import com.wagyufari.dzikirqu.model.events.MenuEvent
import com.wagyufari.dzikirqu.util.*
import com.wagyufari.dzikirqu.util.receiver.KhatamReceiver
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "khatam")
@Parcelize
data class Khatam(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    var name: String? = null,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var state: String? = KhatamStateConstants.ACTIVE,
    var iteration: List<QuranLastRead>? = null,
) : Parcelable{

    companion object{

        fun schedule(context:Context?){
            context?.apply {
                AlarmHelper.cancelQuran(this)
                if (Prefs.khatamReminderSound != -1){
                    when (Prefs.khatamReminderType) {
                        QuranReminderNotificationType.EVERY_1_HOUR -> configureEvery1Hour()
                        QuranReminderNotificationType.EVERY_2_HOUR -> configureEvery2Hour()
                        QuranReminderNotificationType.EVERY_3_HOUR -> configureEvery3Hour()
                        QuranReminderNotificationType.EVERY_4_HOUR -> configureEvery4Hour()
                        QuranReminderNotificationType.EVERY_FARDH_PRAYER -> configureEveryFardhPrayer()
                        else -> {
                            // NOTHING
                        }
                    }
                }
            }
        }

        fun Context.configureEvery1Hour() {
            for (i in 1..24) {
                configure(i, i)
            }
        }

        fun Context.configureEvery2Hour() {
            for (i in 1..24) {
                if (i <= 24){
                    configure(i*2, i)
                }
            }
        }
        fun Context.configureEvery3Hour() {
            for (i in 1..24) {
                if (i <= 24){
                    configure(i*3, i)
                }
            }
        }

        fun Context.configureEvery4Hour() {
            for (i in 1..24) {
                if (i <= 24){
                    configure(i*4, i)
                }
            }
        }


        fun Context.configureEveryFardhPrayer() {
            val fajrHour = Prefs.praytime.fajr?.split(":")?.get(0)?.toIntOrNull() ?: 4
            val fajrMinute = Prefs.praytime.fajr?.split(":")?.get(1)?.toIntOrNull() ?: 4
            val dhuhrHour = Prefs.praytime.dhuhr?.split(":")?.get(0)?.toIntOrNull() ?: 11
            val dhuhrMinute = Prefs.praytime.dhuhr?.split(":")?.get(1)?.toIntOrNull() ?: 34
            val asrHour = Prefs.praytime.asr?.split(":")?.get(0)?.toIntOrNull() ?: 14
            val asrMinute = Prefs.praytime.asr?.split(":")?.get(1)?.toIntOrNull() ?: 49
            val maghribHour = Prefs.praytime.maghrib?.split(":")?.get(0)?.toIntOrNull() ?: 17
            val maghribMinute = Prefs.praytime.maghrib?.split(":")?.get(1)?.toIntOrNull() ?: 44
            val isyaHour = Prefs.praytime.isya?.split(":")?.get(0)?.toIntOrNull() ?: 18
            val isyaMinute = Prefs.praytime.isya?.split(":")?.get(1)?.toIntOrNull() ?: 58
            configure(fajrHour, 1,fajrMinute)
            configure(dhuhrHour,2, dhuhrMinute)
            configure(asrHour,3, asrMinute)
            configure(maghribHour,4, maghribMinute)
            configure(isyaHour,5, isyaMinute)
        }

        private fun Context.configure(hour: Int, requestCode: Int, minute:Int?=0) {
            if (Calendar.getInstance().before(getTimeCalendar(hour, minute?:0))) {
                configureKhatamScheduler(getTimeCalendar(hour, minute?:0).timeInMillis, AlarmHelper.getQuranRequestCode(requestCode))
            }
        }

        private fun getTimeCalendar(hour: Int, minute:Int): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.SECOND, 2)
                set(Calendar.MINUTE, minute)
            }
        }

        fun Context.configureKhatamScheduler(triggerTime: Long, requestCode: Int) {
            val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = KhatamReceiver.getPendingIntent(this, requestCode, Prefs.khatamReminderType)
            alarmMgr.setAlarmClock(AlarmManager.AlarmClockInfo(triggerTime, null), alarmIntent)
        }

        fun configure(context: Context?){
            context?.apply {
                io {
                    if (getKhatamDao().getKhatamSuspend().isEmpty()){
                        io { getKhatamDao().putKhatam(
                            Khatam(
                                id = null,
                                name = null,
                                startDate = getFirstDayOfHijriMonth(),
                                endDate = getLastDayOfHijriMonth(),
                                state = KhatamStateConstants.ACTIVE,
                                iteration = listOf(
                                    QuranLastRead(
                                        surah = 1,
                                        ayah = 1,
                                        page = null,
                                        isSavedFromPage = null,
                                        timestamp = null
                                    ).apply {
                                        lap = 1
                                        state = KhatamStateConstants.ACTIVE
                                    })
                            )
                        ) }
                    } else{
                        val khatam = getKhatamDao().getKhatamSuspend().getOngoing()
                        khatam?.let { khatam->
                            if (Calendar.getInstance().time.after(khatam.endDate) && Calendar.getInstance().time.getHijriMonthName() != khatam.endDate?.getHijriMonthName()){
                                io {
                                    khatam.setState(this, KhatamStateConstants.INACTIVE)
                                    getKhatamDao().putKhatam(
                                        Khatam(
                                            id = null,
                                            name = null,
                                            startDate = getFirstDayOfHijriMonth(),
                                            endDate = getLastDayOfHijriMonth(),
                                            state = KhatamStateConstants.ACTIVE,
                                            iteration = listOf(
                                                QuranLastRead(
                                                    surah = 1,
                                                    ayah = 1,
                                                    page = null,
                                                    isSavedFromPage = null,
                                                    timestamp = null
                                                ).apply {
                                                    lap = 1
                                                    state = KhatamStateConstants.ACTIVE
                                                })
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

fun Khatam.maxValue():Int{
    return (604 * (iteration?.count() ?: 1))
}

@Composable
fun Khatam.currentProgress(context:Context):Int{
    return iteration?.map { context.getAyahLineDao().getAyahLineByKeyLive("${it.surah}:${it.ayah}").observeAsState().value?.firstOrNull()?.page ?: 1}?.sumOf { it } ?: 1
}

fun List<Khatam>.getOngoing(): Khatam? {
    return firstOrNull { it.state == KhatamStateConstants.ACTIVE }
}

fun List<Khatam>.getNotOngoing(): List<Khatam> {
    return filter { it.state != KhatamStateConstants.ACTIVE }
}

fun List<QuranLastRead>.getOngoing(): QuranLastRead? {
    return firstOrNull { it.state == KhatamStateConstants.ACTIVE }
}

fun QuranLastRead.isOngoing(): Boolean {
    return state == KhatamStateConstants.ACTIVE
}

fun QuranLastRead.setAsActive(context:Context, khatam:Khatam){
    context.io{
        context.getKhatamDao().updateKhatam(khatam.apply {
            iteration = iteration?.map {
                it.also {
                    it.state = if (it.lap == this@setAsActive.lap) {
                        KhatamStateConstants.ACTIVE
                    } else {
                        KhatamStateConstants.INACTIVE
                    }
                }
            }
        })
        Prefs.quranLastRead = this
        RxBus.getDefault().send(MenuEvent())
    }
}

fun Khatam.update(surah: Int, ayah: Int, page: Int?=null): Khatam {
    return this.apply {
        iteration = iteration?.map {
            it.apply {
                if (isOngoing()) {
                    this.surah = surah
                    this.ayah = ayah
                    this.page = page
                }
            }
        }
    }
}

fun Khatam.setState(context:Context, state:String){
    context.getKhatamDao().updateKhatam(this.apply {
        this.state = state
    })
}


object KhatamStateConstants {
    const val INACTIVE = "inactive"
    const val ACTIVE = "on_going"
    const val COMPLETED = "completed"
    const val CANCELED = "canceled"
    const val OVERDUE = "overdue"
}

enum class QuranReminderNotificationType {
    NONE,
    EVERY_1_HOUR,
    EVERY_2_HOUR,
    EVERY_3_HOUR,
    EVERY_4_HOUR,
    EVERY_FARDH_PRAYER,
}

fun String.toQuranReminderNotificationType(): QuranReminderNotificationType{
    return when(this) {
        LocaleConstants.EVERY_1_HOUR.locale() -> QuranReminderNotificationType.EVERY_1_HOUR
        LocaleConstants.EVERY_2_HOUR.locale() -> QuranReminderNotificationType.EVERY_2_HOUR
        LocaleConstants.EVERY_3_HOUR.locale() -> QuranReminderNotificationType.EVERY_3_HOUR
        LocaleConstants.EVERY_4_HOUR.locale() -> QuranReminderNotificationType.EVERY_4_HOUR
        LocaleConstants.EVERY_FARDH_PRAYER.locale() -> QuranReminderNotificationType.EVERY_FARDH_PRAYER
        else -> QuranReminderNotificationType.EVERY_1_HOUR
    }
}

fun QuranReminderNotificationType.toStringLocale(): String {
    return when (this) {
        QuranReminderNotificationType.NONE -> "None"
        QuranReminderNotificationType.EVERY_1_HOUR -> LocaleConstants.EVERY_1_HOUR.locale()
        QuranReminderNotificationType.EVERY_2_HOUR -> LocaleConstants.EVERY_2_HOUR.locale()
        QuranReminderNotificationType.EVERY_3_HOUR -> LocaleConstants.EVERY_3_HOUR.locale()
        QuranReminderNotificationType.EVERY_4_HOUR -> LocaleConstants.EVERY_4_HOUR.locale()
        QuranReminderNotificationType.EVERY_FARDH_PRAYER -> LocaleConstants.EVERY_FARDH_PRAYER.locale()
    }
}
