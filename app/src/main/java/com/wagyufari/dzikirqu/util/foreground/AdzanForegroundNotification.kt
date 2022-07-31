package com.wagyufari.dzikirqu.util.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.RxWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.Extra
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.constants.NotificationConstants
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.util.LocaleProvider
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getPreviousPrayerName
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper.getPreviousPrayerTimeString
import io.reactivex.Single


class AdzanForegroundNotification(appContext: Context, workerParams: WorkerParameters):
    RxWorker(appContext, workerParams) {

    val mChannelId = "com.wagyufari.dzikirqu.adzan"
    private var notificationManager: NotificationManager? = null

    val mp = MediaPlayer()

    override fun createWork(): Single<Result> {
        applicationContext.createForegroundNotification("Waktunya shalat ${LocaleProvider.getString(inputData.getString(
            Extra.EXTRA_PRAYER).toString())}")
        return Single.create { emitter->
            mp.setAudioStreamType(AudioManager.STREAM_RING)
            mp.setDataSource(applicationContext, Uri.parse("android.resource://"+applicationContext.packageName +"/"+ Prefs.muadzin))
            mp.prepare()
            mp.start()
            mp.setOnCompletionListener {
                emitter.onSuccess(Result.success())
                applicationContext.createNotification("Waktunya shalat ${inputData.getString(Extra.EXTRA_PRAYER)?.let { LocaleProvider.getString(it) }}")
            }
        }
    }

    override fun onStopped() {
        super.onStopped()
        mp.stop()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelId, NotificationConstants.mChannelNameAdzan, importance)
        notificationManager?.createNotificationChannel(mChannel)
    }

    private fun Context.createForegroundNotification(text: String) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        val title = text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        WorkManager.getInstance(applicationContext)

        val notification = NotificationCompat.Builder(applicationContext, mChannelId)
            .setContentTitle(title)
            .setContentText("\uD83D\uDD52 " + String.format(LocaleConstants.N_HOUR_N_AT_N.locale(), Prefs.praytime.getPreviousPrayerName(),Prefs.praytime.getPreviousPrayerTimeString(),Prefs.userCity))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDeleteIntent(WorkManager.getInstance(applicationContext).createCancelPendingIntent(id))
            .setSmallIcon(R.drawable.ic_dzikir)
            .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
            .build()

        notificationManager?.notify(0, notification)
    }
}