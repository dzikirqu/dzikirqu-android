package com.wagyufari.dzikirqu.util.foreground

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.constants.NotificationConstants
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.ui.main.MainActivity
import com.wagyufari.dzikirqu.ui.praytime.PraytimeActivity
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.receiver.TickReceiver
import com.wagyufari.dzikirqu.util.getHijriDate
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getCurrentPrayerName
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getCurrentPrayerTimeString
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getTimeUntilNextPrayerString

class PraytimeForegroundService : Service() {

    private var mNotificationManager: NotificationManager? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(TickReceiver(), IntentFilter(Intent.ACTION_TIME_TICK))
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startForeground(321, prepareNotification())
    }

    private fun prepareNotification(): Notification {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        Prefs.praytime.getCurrentPrayerTimeString()
        val notification = NotificationCompat.Builder(applicationContext, FOREGROUND_CHANNEL_ID)
            .setContentTitle("\uD83D\uDD52 " + String.format(LocaleConstants.N_HOUR_N_AT_N.locale(), Prefs.praytime.getCurrentPrayerName(),Prefs.praytime.getCurrentPrayerTimeString(),Prefs.userCity))
            .setContentText(String.format(LocaleConstants.VIEW_PRAYER_TIMES_IN_N.locale(), Prefs.userCity))
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(
                this@PraytimeForegroundService,
                0,
                Intent(this@PraytimeForegroundService, PraytimeActivity::class.java),
                0
            ))
            .setSmallIcon(R.drawable.ic_dzikir)
            .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
            .build()

        return notification
    }
    
    fun getBigLayout():RemoteViews{
        return RemoteViews(this@PraytimeForegroundService.packageName, R.layout.praytime_widget_2).apply {
            setTextViewText(R.id.nextPrayerTitle, LocaleConstants.NEXT_PRAYER.locale())
            setTextViewText(R.id.nextPrayerTime, Prefs.praytime.getCurrentPrayerTimeString())
            setTextViewText(R.id.nextPrayerUntil, Prefs.praytime.getTimeUntilNextPrayerString())
            setTextViewText(R.id.fajrTime, Prefs.praytime.fajr)
            setTextViewText(R.id.dhuhrTime, Prefs.praytime.dhuhr)
            setTextViewText(R.id.asrTime, Prefs.praytime.asr)
            setTextViewText(R.id.maghribTime, Prefs.praytime.maghrib)
            setTextViewText(R.id.isyaTime, Prefs.praytime.isya)
            setTextViewText(R.id.textAddress, Prefs.userCity)
            setTextViewText(R.id.textHijriDate, getHijriDate())
            setOnClickPendingIntent(
                R.id.lastRead,
                PendingIntent.getActivity(
                    this@PraytimeForegroundService,
                    0,
                    ReadActivity.newSurahIntent(
                        this@PraytimeForegroundService,
                        Prefs.quranLastRead.surah,
                        Prefs.quranLastRead.ayah
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            setOnClickPendingIntent(
                R.id.rootPraytime,
                PendingIntent.getActivity(
                    this@PraytimeForegroundService,
                    0,
                    Intent(this@PraytimeForegroundService, PraytimeActivity::class.java),
                    0
                )
            )
            setOnClickPendingIntent(
                R.id.rootHeader,
                PendingIntent.getActivity(
                    this@PraytimeForegroundService,
                    0,
                    Intent(this@PraytimeForegroundService, MainActivity::class.java),
                    0
                )
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(321, prepareNotification())
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(
            FOREGROUND_CHANNEL_ID,
            NotificationConstants.mChannelNameAdzan,
            importance
        )
        mNotificationManager?.createNotificationChannel(mChannel)
    }

    companion object {
        private const val FOREGROUND_CHANNEL_ID = "foreground_channel_id"
        private val TAG = PraytimeForegroundService::class.java.simpleName
    }

}