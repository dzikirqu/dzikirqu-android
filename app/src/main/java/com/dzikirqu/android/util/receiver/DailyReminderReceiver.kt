package com.dzikirqu.android.util.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.dzikirqu.android.R
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.constants.NotificationConstants.mChannelNameDailyReminder
import com.dzikirqu.android.ui.main.MainActivity
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main
import java.util.*

class DailyReminderReceiver : BroadcastReceiver() {

    val mChannelId = "com.dzikirqu.android.daily"
    private var notificationManager: NotificationManager? = null

    companion object {

        const val EXTRA_DAILY_REMINDER = "extra_daily_reminder"
        const val EXTRA_TRIGGER_TIME = "extra_trigger_time"

        fun getPendingIntent(
            context: Context,
            requestCode: Int,
            triggerTime: Long,
            subtitle: String,
        ): PendingIntent {
            return Intent(context, DailyReminderReceiver::class.java).let { intent ->
                intent.putExtra(EXTRA_TRIGGER_TIME, triggerTime)
                intent.putExtra(EXTRA_DAILY_REMINDER, subtitle)
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        val triggerTime = intent?.getLongExtra(EXTRA_TRIGGER_TIME, 0) ?: 0
        if (Calendar.getInstance().timeInMillis in triggerTime - 2000..triggerTime + 2000) {
            val dailyReminder = intent?.getStringExtra(EXTRA_DAILY_REMINDER)
            context.createNotification(dailyReminder.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelId, mChannelNameDailyReminder, importance)
        notificationManager?.createNotificationChannel(mChannel)
    }

    private fun Context.createNotification(subtitle:String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        io {
            main {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel()
                }

                val notification = NotificationCompat.Builder(applicationContext, mChannelId)
                    .setContentTitle(LocaleConstants.DAILY_PRAYERS.locale())
                    .setContentText(subtitle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, MainActivity::class.java),
                        0
                    ))
                    .setSmallIcon(R.drawable.ic_dzikir)
                    .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
                    .build()

                notificationManager?.notify(2, notification)
            }
        }
    }
}

