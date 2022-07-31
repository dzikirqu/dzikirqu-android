package com.wagyufari.dzikirqu.base

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.pixplicity.easyprefs.library.Prefs
import com.wagyufari.dzikirqu.model.Khatam
import com.wagyufari.dzikirqu.util.LocaleProvider.Companion.init
import com.wagyufari.dzikirqu.util.praytimes.Praytime
import com.wagyufari.dzikirqu.util.textfont.TypeFactory
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : SplitCompatApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        SplitCompat.install(this)
    }

    override fun onCreate() {
        MultiDex.install(this)
        init(this)
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
        Praytime.schedule(this)
        Khatam.configure(this)
        super.onCreate()
        instance = this
//        Thread.setDefaultUncaughtExceptionHandler(object: CrashHandler(applicationContext){})
        AppCompatDelegate.setDefaultNightMode(com.wagyufari.dzikirqu.data.Prefs.appTheme)
    }


    private var mFontFactory: TypeFactory? = null
    fun getTypeFace(type: Int): Typeface? {
        mFontFactory = TypeFactory(this)
        mFontFactory?.let {
            return when (type) {
                Constants.MEDIUM -> mFontFactory?.medium
                Constants.BOLD -> mFontFactory?.bold
                Constants.UTHMAN -> mFontFactory?.uthman
                Constants.BLACK -> mFontFactory?.black
                Constants.CASLON -> mFontFactory?.caslon
                else -> mFontFactory?.regular
            }
        }
        return null
    }

    private interface Constants {
        companion object {
            const val MEDIUM = 1
            const val BOLD = 2
            const val UTHMAN = 3
            const val BLACK = 4
            const val CASLON = 5
        }
    }

    companion object {
        @get:Synchronized
        var instance: BaseApplication? = null
            private set
    }
}
