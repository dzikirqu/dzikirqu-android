package com.wagyufari.dzikirqu.util.praytimes

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.model.DailyReminder
import com.wagyufari.dzikirqu.model.Khatam
import com.wagyufari.dzikirqu.model.PrayerTime
import com.wagyufari.dzikirqu.util.AlarmHelper
import com.wagyufari.dzikirqu.util.BooleanUtil.isLocationGranted
import com.wagyufari.dzikirqu.util.PraytimeWidget
import com.wagyufari.dzikirqu.util.foreground.PraytimeForegroundService
import com.wagyufari.dzikirqu.util.receiver.AdzanReceiver
import com.wagyufari.dzikirqu.util.receiver.TickReceiver
import java.util.*

class Praytime {
    
    companion object{
        fun configureForegroundService(activity: Activity?){
            activity?.apply {
                registerReceiver(TickReceiver(), IntentFilter(Intent.ACTION_TIME_TICK))
                registerReceiver(TickReceiver(), IntentFilter(Intent.ACTION_TIME_CHANGED))
            }
        }
        fun schedule(context:Context?){
            context?.apply{
                PraytimeWidget.update(this)
                Khatam.schedule(this)
                DailyReminder.schedule(this)

                if (Prefs.userCoordinates.latitude == 0.0 || Prefs.userCoordinates.longitude == 0.0) {
                    return
                }

                val praytime = PrayerTimeHelper.getPrayerTimeFromPrefs(this)

                if (Calendar.getInstance().before(getFajrTime(praytime))) {
                    configureAdzanScheduler(
                        getFajrTime(praytime).timeInMillis,
                        AlarmHelper.mFajrRequestCode,
                        PraytimeType.Fajr,
                        Prefs.ringFajr
                    )
                }

                if (Calendar.getInstance().before(getDhuhrTime(praytime))) {
                    configureAdzanScheduler(
                        getDhuhrTime(praytime).timeInMillis,
                        AlarmHelper.mDhuhrRequestCode,
                        PraytimeType.Dhuhr,
                        Prefs.ringDhuhr
                    )
                }

                if (Calendar.getInstance().before(getAsrTime(praytime))) {
                    configureAdzanScheduler(
                        getAsrTime(praytime).timeInMillis,
                        AlarmHelper.mAsrRequestCode,
                        PraytimeType.Asr,
                        Prefs.ringAsr
                    )
                }

                if (Calendar.getInstance().before(getMaghribTime(praytime))) {
                    configureAdzanScheduler(
                        getMaghribTime(praytime).timeInMillis,
                        AlarmHelper.mMaghribRequestCode,
                        PraytimeType.Maghrib,
                        Prefs.ringMaghrib
                    )
                }

                if (Calendar.getInstance().before(getIsyaTime(praytime))) {
                    configureAdzanScheduler(
                        getIsyaTime(praytime).timeInMillis,
                        AlarmHelper.mIsyaRequestCode,
                        PraytimeType.Isya,
                        Prefs.ringIsya
                    )
                }
            }
        }

        private fun Context.configureAdzanScheduler(triggerTime: Long, requestCode: Int, prayer: PraytimeType, ringType: Int) {
            val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = AdzanReceiver.getPendingIntent(this, requestCode, triggerTime, prayer, ringType)
            alarmMgr.cancel(alarmIntent)
            alarmMgr.setAlarmClock(AlarmManager.AlarmClockInfo(triggerTime, null), alarmIntent)
        }

        fun getFajrTime(prayTime: PrayerTime): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, prayTime.fajr?.split(":")?.get(0)?.toInt() ?: 4)
                set(Calendar.MINUTE, prayTime.fajr?.split(":")?.get(1)?.toInt() ?: 4)
                set(Calendar.SECOND, 5)
                set(Calendar.MILLISECOND, 0)
            }
        }

        fun getDhuhrTime(prayTime: PrayerTime): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, prayTime.dhuhr?.split(":")?.get(0)?.toInt() ?: 11)
                set(Calendar.MINUTE, prayTime.dhuhr?.split(":")?.get(1)?.toInt() ?: 34)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        fun getAsrTime(prayTime: PrayerTime): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, prayTime.asr?.split(":")?.get(0)?.toInt() ?: 14)
                set(Calendar.MINUTE, prayTime.asr?.split(":")?.get(1)?.toInt() ?: 49)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        fun getMaghribTime(prayTime: PrayerTime): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, prayTime.maghrib?.split(":")?.get(0)?.toInt() ?: 17)
                set(Calendar.MINUTE, prayTime.maghrib?.split(":")?.get(1)?.toInt() ?: 44)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        fun getIsyaTime(prayTime: PrayerTime): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, prayTime.isya?.split(":")?.get(0)?.toInt() ?: 18)
                set(Calendar.MINUTE, prayTime.isya?.split(":")?.get(1)?.toInt() ?: 56)
                set(Calendar.SECOND, 10)
                set(Calendar.MILLISECOND, 0)
            }
        }
    }


}

enum class PraytimeType {
    Refresh,
    Loop,
    Fajr,
    Dhuhr,
    Asr,
    Maghrib,
    Isya
}