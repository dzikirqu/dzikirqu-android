package com.dzikirqu.android.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.util.receiver.DailyReminderReceiver
import com.dzikirqu.android.util.receiver.KhatamReceiver

object AlarmHelper {

    val mFajrRequestCode = 1111
    val mDhuhrRequestCode = 2222
    val mAsrRequestCode = 3333
    val mMaghribRequestCode = 4444
    val mIsyaRequestCode = 5555

    private val mQuranRequestCode = 6236
    private val mDailyRequestCode = 3113

    fun cancelQuran(context: Context) {
        context.apply {
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
                for (i in 0..100){
                    cancel(KhatamReceiver.getPendingIntent(context, "$mQuranRequestCode$i".toInt(), Prefs.khatamReminderType))
                }
            }
        }
    }
    fun cancelDailyPrayer(context: Context) {
        context.apply {
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
                for (i in 0..100){
                    cancel(PendingIntent.getBroadcast(
                        context,
                        getDailyReminderRequestCode(i),
                        Intent(context, DailyReminderReceiver::class.java),
                        PendingIntent.FLAG_IMMUTABLE
                    ))
                }
            }
        }
    }

    fun getQuranRequestCode(requestCode:Int):Int{
        return "$mQuranRequestCode$requestCode".toInt()
    }

    fun getDailyReminderRequestCode(requestCode:Int):Int{
        return "$mQuranRequestCode$requestCode".toInt()
    }

}