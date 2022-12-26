package com.dzikirqu.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dzikirqu.android.util.praytimes.Praytime


class AutoStartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Praytime.schedule(context)
    }
}
