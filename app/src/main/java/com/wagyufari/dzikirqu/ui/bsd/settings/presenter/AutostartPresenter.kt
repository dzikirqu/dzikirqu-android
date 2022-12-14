package com.wagyufari.dzikirqu.ui.bsd.settings.presenter

import android.content.ComponentName
import android.content.Intent
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsPresenter

fun SettingsPresenter.autostart(){
    viewDataBinding.textAutostart.setOnClickListener {
        val intent = Intent()
        intent.component = ComponentName(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
        it.context.startActivity(intent)
    }
}