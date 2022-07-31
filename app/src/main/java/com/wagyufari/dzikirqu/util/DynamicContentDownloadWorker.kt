package com.wagyufari.dzikirqu.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.constants.NotificationConstants.mDownloadChannelName
import com.wagyufari.dzikirqu.constants.NotificationConstants.mDynamicContentId
import io.reactivex.Single

class DynamicContentDownloadWorker(appContext: Context, workerParams: WorkerParameters) :
    RxWorker(appContext, workerParams) {

    val mSessionId = 0
    val mChannelId = "com.wagyufari.dzikirqu.download"

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override fun createWork(): Single<Result> {
        val splitInstallManager = SplitInstallManagerFactory.create(applicationContext);
        setForegroundAsync(createForegroundInfo(0,0))
        val request = SplitInstallRequest.newBuilder()
            .addModule("pagedquran")
            .build()
        return Single.create{ emitter->
            splitInstallManager.registerListener {
                when(it.status()){
                    SplitInstallSessionStatus.DOWNLOADING->{
                        setForegroundAsync(createForegroundInfo(it.bytesDownloaded(), it.totalBytesToDownload()))
                    }
                    SplitInstallSessionStatus.CANCELED->{
                        emitter.onSuccess(Result.success())
                    }
                    SplitInstallSessionStatus.INSTALLED->{
                        createInfo(LocaleConstants.AL_QURAN_DATA_HAS_SUCCESSFULLY_BEEN_DOWNLOADED.locale())
                        emitter.onSuccess(Result.success())
                    }
                }
            }
            splitInstallManager.startInstall(request)
        }
    }

    private fun createInfo(message:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, mChannelId)
            .setSilent(true)
            .setVibrate(longArrayOf())
            .setContentTitle(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
            .build()

        notificationManager.notify(mDynamicContentId, notification)
    }

    private fun createForegroundInfo(progress: Long, length: Long): ForegroundInfo {
        val title = LocaleConstants.DOWNLOADING_FONTS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, mChannelId)
            .setSilent(true)
            .setVibrate(longArrayOf())
            .setContentTitle(title)
            .setContentText(
                "${
                    String.format(
                        "%.1f",
                        (progress.toDouble() / 1000000)
                    )
                } MB / ${String.format("%.1f", (length.toDouble() / 1000000))} MB"
            )
            .setTicker(title)
            .setProgress(length.toInt(), progress.toInt(), false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(applicationContext.resources.getColor(R.color.colorPrimary))
            .build()

        return ForegroundInfo(mDynamicContentId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(mChannelId, mDownloadChannelName, importance)
        notificationManager.createNotificationChannel(mChannel)
    }

}