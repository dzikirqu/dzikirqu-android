package com.wagyufari.dzikirqu.util.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wagyufari.dzikirqu.util.BooleanUtil.isLocationGranted
import com.wagyufari.dzikirqu.util.PraytimeWidget
import com.wagyufari.dzikirqu.util.foreground.PraytimeForegroundService
import com.wagyufari.dzikirqu.util.praytimes.Praytime

class TickReceiver:BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Praytime.schedule(p0)
        PraytimeWidget.update(p0)
    }
}