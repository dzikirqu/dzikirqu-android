package com.dzikirqu.android.util.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.dzikirqu.android.R
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.constants.NotificationConstants.mChannelNameKhatam
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.getSurahDao
import com.dzikirqu.android.model.QuranReminderNotificationType
import com.dzikirqu.android.ui.main.MainActivity
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main

class KhatamReceiver : BroadcastReceiver() {

    val mChannelId = "com.dzikirqu.android.khatam"
    private var notificationManager: NotificationManager? = null
    val mp = MediaPlayer()

    companion object {

        const val EXTRA_NOTIFICATION_TYPE = "extra_notification_type"

        fun getPendingIntent(
            context: Context,
            requestCode: Int,
            notificationType: QuranReminderNotificationType,
        ): PendingIntent {
            return Intent(context, KhatamReceiver::class.java).let { intent ->
                intent.putExtra(EXTRA_NOTIFICATION_TYPE, notificationType.name)
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_MUTABLE,
                )
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

            context.createNotification()

            if (Prefs.khatamReminderSound != 0 && Prefs.khatamReminderSound != -1) {
                mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
                mp.setDataSource(
                    context,
                    Uri.parse("android.resource://" + context.packageName + "/" + Prefs.khatamReminderSound)
                )
                mp.prepare()
                mp.start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelId, mChannelNameKhatam, importance)
        notificationManager?.createNotificationChannel(mChannel)
    }

    private fun Context.createNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        io {
            val surah = getSurahDao().getSurahById(Prefs.quranLastRead.surah).firstOrNull()
            main {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel()
                }

                val intent = TaskStackBuilder.create(this@createNotification)
                    .addNextIntent(MainActivity.newIntent(this@createNotification))
                    .addNextIntent(
                        ReadActivity.newSurahIntent(
                            this@createNotification,
                            Prefs.quranLastRead.surah,
                            Prefs.quranLastRead.ayah
                        )
                    )
                    .getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

                val notification = NotificationCompat.Builder(applicationContext, mChannelId)
                    .setContentTitle(LocaleConstants.ITS_TIME_TO_READ_THE_QURAN.locale())
                    .setContentText(LocaleConstants.LAST_READ_COLON.locale() + " ${surah?.name} ${Prefs.quranLastRead.ayah}")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_dzikir)
                    .setContentIntent(intent)
                    .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
                    .build()

                notificationManager?.notify(0, notification)
            }
        }
    }
}

