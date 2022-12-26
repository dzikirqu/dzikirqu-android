package com.dzikirqu.android.util

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.dzikirqu.android.R
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.ui.main.MainActivity
import com.dzikirqu.android.ui.praytime.PraytimeActivity
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.util.praytimes.PrayerTimeHelper.getCurrentPrayerTimeString
import com.dzikirqu.android.util.praytimes.PrayerTimeHelper.getTimeUntilNextPrayerString


class PraytimeWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateLayout(context, appWidgetManager, appWidgetIds)
    }

    companion object{

        fun update(context:Context?) {
            context?.apply {
                updateWidget1(this)
                updateWidget2(this)
            }
        }
    }
}

class PraytimeWidget2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateLayout(context, appWidgetManager, appWidgetIds)
    }
}


fun updateLayout(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    for (appWidgetId in appWidgetIds) {
        if (appWidgetManager.getAppWidgetInfo(appWidgetId).initialLayout == R.layout.praytime_widget_1) {
            RemoteViews(context.packageName, R.layout.praytime_widget_1).apply {
                setTextViewText(R.id.fajrTime, Prefs.praytime.fajr)
                setTextViewText(R.id.dhuhrTime, Prefs.praytime.dhuhr)
                setTextViewText(R.id.asrTime, Prefs.praytime.asr)
                setTextViewText(R.id.maghribTime, Prefs.praytime.maghrib)
                setTextViewText(R.id.isyaTime, Prefs.praytime.isya)
                setTextViewText(R.id.textAddress, Prefs.userCity)
                setOnClickPendingIntent(
                    R.id.rootPraytime,
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, PraytimeActivity::class.java),
                        0
                    )
                )
                appWidgetManager.updateAppWidget(appWidgetId, this)
            }
        }
        if (appWidgetManager.getAppWidgetInfo(appWidgetId).initialLayout == R.layout.praytime_widget_2) {
            RemoteViews(context.packageName, R.layout.praytime_widget_2).apply {
                setTextViewText(R.id.nextPrayerTitle, LocaleConstants.NEXT_PRAYER.locale())
                setTextViewText(R.id.nextPrayerTime, Prefs.praytime.getCurrentPrayerTimeString())
//                setTextViewText(R.id.nextPrayerUntil, SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().time))
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
                        context,
                        0,
                        ReadActivity.newSurahIntent(
                            context,
                            Prefs.quranLastRead.surah,
                            Prefs.quranLastRead.ayah
                        ),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
                setOnClickPendingIntent(
                    R.id.rootPraytime,
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, PraytimeActivity::class.java),
                        0
                    )
                )
                setOnClickPendingIntent(
                    R.id.rootHeader,
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java),
                        0
                    )
                )
                appWidgetManager.updateAppWidget(appWidgetId, this)
            }
        }
    }
}



fun updateWidget1(context: Context) {
    val man = AppWidgetManager.getInstance(context)
    val ids = man.getAppWidgetIds(ComponentName(context, PraytimeWidget::class.java))
    val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
    context.sendBroadcast(updateIntent)
}


fun updateWidget2(context: Context) {
    val man = AppWidgetManager.getInstance(context)
    val ids = man.getAppWidgetIds(ComponentName(context, PraytimeWidget2::class.java))
    val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
    context.sendBroadcast(updateIntent)
}
