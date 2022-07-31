package com.wagyufari.dzikirqu.util.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.Extra
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.constants.NotificationConstants.mChannelNameAdzan
import com.wagyufari.dzikirqu.constants.RingType
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.util.foreground.AdzanForegroundNotification
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getPreviousPrayerName
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getPreviousPrayerTimeString
import com.wagyufari.dzikirqu.util.praytimes.Praytime
import com.wagyufari.dzikirqu.util.praytimes.PraytimeType
import java.util.*

class AdzanReceiver : BroadcastReceiver() {

    val mChannelId = "com.wagyufari.dzikirqu.adzan"
    private var notificationManager: NotificationManager? = null

    companion object {
        const val EXTRA_PRAYER = "extra_prayer"
        const val EXTRA_RING_TYPE = "extra_ring"
        const val EXTRA_TRIGGER_TIME = "extra_trigger_time"
        fun getPendingIntent(
            context: Context,
            requestCode: Int,
            triggerTime: Long,
            prayerType: PraytimeType,
            ringType: Int
        ): PendingIntent {
            return Intent(context, AdzanReceiver::class.java).let { intent ->
                intent.putExtra(EXTRA_PRAYER, prayerType.name)
                intent.putExtra(EXTRA_TRIGGER_TIME, triggerTime)
                intent.putExtra(EXTRA_RING_TYPE, ringType)
                PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        val triggerTime = intent?.getLongExtra(EXTRA_TRIGGER_TIME, 0) ?: 0
        val prayer = PraytimeType.valueOf(intent?.getStringExtra(EXTRA_PRAYER) ?: PraytimeType.Refresh.name)
        val ringType = intent?.getIntExtra(EXTRA_RING_TYPE, RingType.SILENT)

        if (Calendar.getInstance().timeInMillis in triggerTime - 2000..triggerTime + 2000) {
            if (ringType == RingType.SOUND && Prefs.userCoordinates.latitude != 0.0 && Prefs.userCoordinates.longitude != 0.0) {
                WorkManager.getInstance(context)
                    .enqueue(OneTimeWorkRequestBuilder<AdzanForegroundNotification>()
                        .setInputData(workDataOf(Extra.EXTRA_PRAYER to prayer.name))
                        .build())
            } else if (ringType == RingType.NOTIFICATION){
                context.createNotification("Waktunya shalat ${prayer.name.locale()}")
            }
        }
        Praytime.schedule(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelId, mChannelNameAdzan, importance)
        notificationManager?.createNotificationChannel(mChannel)
    }

    private fun Context.createNotification(text: String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        val title = "\uD83D\uDD52 " + String.format(LocaleConstants.N_HOUR_N_AT_N.locale(), Prefs.praytime.getPreviousPrayerName(),Prefs.praytime.getPreviousPrayerTimeString(),Prefs.userCity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, mChannelId)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSmallIcon(R.drawable.ic_dzikir)
            .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
            .build()

        notificationManager?.notify(0, notification)
    }
}

