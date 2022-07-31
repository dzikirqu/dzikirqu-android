package com.wagyufari.dzikirqu.model

import android.app.AlarmManager
import android.content.Context
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.dao.getDailyReminderDao
import com.wagyufari.dzikirqu.data.room.dao.getDailyReminderParentDao
import com.wagyufari.dzikirqu.util.AlarmHelper
import com.wagyufari.dzikirqu.util.io
import com.wagyufari.dzikirqu.util.receiver.DailyReminderReceiver
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "dailyReminderParent")
@Parcelize
data class DailyReminderParent(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "startTime")
    var startTime: String? = null,
    @ColumnInfo(name = "endTime")
    var endTime: String? = null,
    @ColumnInfo(name = "startTimePrayerConstraint")
    var startTimePrayerConstraint: Int? = null,
    @ColumnInfo(name = "startTimePrayerConstraintDifference")
    var startTimePrayerConstraintDifference: Int? = null,
    @ColumnInfo(name = "endTimePrayerConstraint")
    var endTimePrayerConstraint: Int? = null,
    @ColumnInfo(name = "endTimePrayerConstraintDifference")
    var endTimePrayerConstraintDifference: Int? = null,
) : Parcelable

@Entity(tableName = "dailyReminder")
@Parcelize
data class DailyReminder(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "typeId")
    var typeId: Int? = null,
    @ColumnInfo(name = "type")
    var type: DailyReminderType,
    @ColumnInfo(name = "parentId")
    var parentId: Int,
) : Parcelable {
    companion object {

        fun schedule(context:Context?){
            context?.apply {
                AlarmHelper.cancelDailyPrayer(this)
                io {
                    getDailyReminderParentDao().getDailyReminderSuspend().forEachIndexed { index, dailyReminderParent ->
                        configure(dailyReminderParent, index)
                    }
                }
            }}

        private fun Context.configure(parent:DailyReminderParent, requestCode: Int) {
            configureDailyReceiver(parent.getStartTimeDate().time, AlarmHelper.getDailyReminderRequestCode(requestCode), parent)
        }


        fun Context.configureDailyReceiver(triggerTime: Long, requestCode: Int, parent:DailyReminderParent) {
            val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = DailyReminderReceiver.getPendingIntent(this, requestCode, triggerTime, parent.title.toString())
            alarmMgr.setAlarmClock(AlarmManager.AlarmClockInfo(triggerTime, null), alarmIntent)
        }

        fun DailyReminderParent.getStartTime(): String {
            startTime?.let {
                return it
            } ?: kotlin.run {
                return getTimeStringFromPrayerTime(startTimePrayerConstraint!!,
                    startTimePrayerConstraintDifference!!)
            }
        }

        fun DailyReminderParent.getStartTimeUIString(): String {
            startTimePrayerConstraint?.let {
                return if (startTimePrayerConstraintDifference!! < 0){
                    String.format(LocaleConstants.N_MINUTES_BEFORE_N_PRAYER.locale(), (startTimePrayerConstraintDifference!! * -1).toString(), getPrayerFromInt(startTimePrayerConstraint!!))
                } else{
                    String.format(LocaleConstants.N_MINUTES_AFTER_N_PRAYER.locale(), (startTimePrayerConstraintDifference!!).toString(), getPrayerFromInt(startTimePrayerConstraint!!))
                }
            }?: kotlin.run {
                return startTime.toString()
            }
        }

        fun DailyReminderParent.getEndTimeUIString(): String {
            endTimePrayerConstraint?.let {
                return if (endTimePrayerConstraintDifference!! < 0){
                    String.format(LocaleConstants.N_MINUTES_BEFORE_N_PRAYER.locale(), (endTimePrayerConstraintDifference!! * -1).toString(), getPrayerFromInt(endTimePrayerConstraint!!))
                } else{
                    String.format(LocaleConstants.N_MINUTES_AFTER_N_PRAYER.locale(), (endTimePrayerConstraintDifference!!).toString(), getPrayerFromInt(endTimePrayerConstraint!!))
                }
            }?: kotlin.run {
                return endTime.toString()
            }
        }

        fun getPrayerFromInt(prayer:Int):String{
            return when(prayer){
                1-> LocaleConstants.FAJR.locale()
                2-> LocaleConstants.DHUHR.locale()
                3-> LocaleConstants.ASR.locale()
                4-> LocaleConstants.MAGHRIB.locale()
                else -> LocaleConstants.ISYA.locale()
            }
        }

        fun DailyReminderParent.getStartTimeDate(): Date {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, getStartTime().split(":")[0].toInt())
                set(Calendar.MINUTE, getStartTime().split(":")[1].toInt())
            }.time
        }

        fun DailyReminderParent.getEndTimeDate(): Date {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, getEndTime().split(":")[0].toInt())
                set(Calendar.MINUTE, getEndTime().split(":")[1].toInt())
            }.time
        }

        fun DailyReminderParent.getEndTime(): String {
            endTime?.let {
                return it
            } ?: kotlin.run {
                return getTimeStringFromPrayerTime(endTimePrayerConstraint!!,
                    endTimePrayerConstraintDifference!!)
            }
        }

        fun getTimeStringFromPrayerTime(prayerTimeId: Int, difference: Int): String {
            return when (prayerTimeId) {
                1 -> getTimeStringWithDifference(Prefs.praytime.fajr ?: "00:00", difference)
                2 -> getTimeStringWithDifference(Prefs.praytime.dhuhr ?: "00:00", difference)
                3 -> getTimeStringWithDifference(Prefs.praytime.asr ?: "00:00", difference)
                4 -> getTimeStringWithDifference(Prefs.praytime.maghrib ?: "00:00", difference)
                5 -> getTimeStringWithDifference(Prefs.praytime.isya ?: "00:00", difference)
                else -> "00:00"
            }
        }

        fun getTimeStringWithDifference(time: String, difference: Int): String {
            val hour = time.split(":")[0].toInt()
            val minutes = time.split(":")[1].toInt()
            return SimpleDateFormat("HH:mm").format(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minutes)
                add(Calendar.MINUTE, difference)
            }.time)
        }

        fun configureDefault(context: Context) {
            context.apply {
//                03.00 - 10 Menit Sebelum Shubuh
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Bangun tidur, Shalat Tahajud dan Persiapan Shubuh",
                    startTime = "03:00",
                    endTimePrayerConstraint = 1,
                    endTimePrayerConstraintDifference = -10,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 53,
                        type = DailyReminderType.Prayer,
                        parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 30,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 39,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 3,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 4,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 31,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                10 Menit sebelum Adzan Shubuh - 5 Menit Setelah Adzan
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Shalat Shubuh",
                    startTimePrayerConstraint = 1,
                    startTimePrayerConstraintDifference = -10,
                    endTimePrayerConstraint = 1,
                    endTimePrayerConstraintDifference = 5,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 40,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 49,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 32,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 48,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 33,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 52,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                5 Menit Setelah Shubuh - 06:00

                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Dzikir Pagi",
                    startTimePrayerConstraint = 1,
                    startTimePrayerConstraintDifference = 5,
                    endTime = "06:00",
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 29,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 28,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                06:00 - 10:00
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Persiapan, Berangkat Kerja/Sekolah",
                    startTime = "06:00",
                    endTime = "10:00",
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 39,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 3,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 58,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 59,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 8,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 42,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 15,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 102,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 38,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 67,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 68,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 73,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                10:00 - 10 menit sebelum dzuhur

                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Aktifitas Kerja/Belajar/IRT",
                    startTime = "10:00",
                    endTimePrayerConstraint = 2,
                    endTimePrayerConstraintDifference = -10,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 12,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 92,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 102,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 36,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 104,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                10 Menit sebelum dzuhur - 5 menit setelah dzuhur
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Shalat Dzuhur",
                    startTimePrayerConstraint = 2,
                    startTimePrayerConstraintDifference = -10,
                    endTimePrayerConstraint = 2,
                    endTimePrayerConstraintDifference = 5,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 40,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 49,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 32,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 48,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 33,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 15,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 52,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 4,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 31,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                10 menit setelah dzuhur - 13:00
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Istirahat",
                    startTimePrayerConstraint = 2,
                    startTimePrayerConstraintDifference = 10,
                    endTime = "13:00",
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 29,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 58,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 59,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 95,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 96,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                13.01 - 10 Menit Sebelum Adzan Ashar

                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Aktifitas Kerja/Belajar/IRT",
                    startTime = "13:00",
                    endTimePrayerConstraint = 3,
                    endTimePrayerConstraintDifference = -10,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 0,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 18,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 37,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 100,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

//                10 menit sebelum ashar - 5 menit setelah ashar
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Shalat Ashar",
                    startTimePrayerConstraint = 3,
                    startTimePrayerConstraintDifference = -10,
                    endTimePrayerConstraint = 3,
                    endTimePrayerConstraintDifference = 5,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 40,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 49,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 32,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 48,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 33,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 15,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 52,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 4,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 31,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

                // 10 menit setelah ashar - 16:00
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Dzikir Sore",
                    startTimePrayerConstraint = 3,
                    startTimePrayerConstraintDifference = 10,
                    endTime = "16:00",
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 5,
                        type = DailyReminderType.Prayer, parentId = it.toInt()))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 29,
                        type = DailyReminderType.Prayer, parentId = it.toInt()))
                }

                // 16:00 - 10 menit sebelum maghrib
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Persiapan, Pulang Kerja/Sekolah",
                    startTime = "16:00",
                    endTimePrayerConstraint = 4,
                    endTimePrayerConstraintDifference = -10,
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 93,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 94,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 95,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 67,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 68,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Shalat Maghrib",
                    startTimePrayerConstraint = 4,
                    startTimePrayerConstraintDifference = -10,
                    endTimePrayerConstraint = 4,
                    endTimePrayerConstraintDifference = 10
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 40,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 49,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 32,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 48,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 33,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 15,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 52,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 4,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 31,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Istirahat",
                    startTimePrayerConstraint = 4,
                    startTimePrayerConstraintDifference = 10,
                    endTimePrayerConstraint = 5,
                    endTimePrayerConstraintDifference = -10
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 29,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 58,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 59,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }
                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Shalat Isya",
                    startTimePrayerConstraint = 5,
                    startTimePrayerConstraintDifference = -10,
                    endTimePrayerConstraint = 5,
                    endTimePrayerConstraintDifference = 10
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 40,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 49,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 32,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 48,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 33,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 15,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 52,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 4,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 31,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }

                getDailyReminderParentDao().putDailyReminder(DailyReminderParent(
                    title = "Tidur dan Istirahat",
                    startTimePrayerConstraint = 5,
                    startTimePrayerConstraintDifference = 10,
                    endTime = "22:00"
                )).let {
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 4,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 31,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 39,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 3,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 34,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 57,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                    getDailyReminderDao().putDailyReminder(DailyReminder(
                        typeId = 27,
                        type = DailyReminderType.Prayer, parentId = it.toInt()
                    ))
                }
            }
        }
    }
}

enum class DailyReminderType {
    Prayer, Quran
}