package com.dzikirqu.android.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.Prefs

object BooleanUtil {

    fun Context.isReadAndWriteExternalStorageGranted(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun Context.isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun Context.isStorageGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isXiaomi():Boolean{
        return "xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)
    }

    fun BaseViewModel<*>.isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            dataManager.mContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            dataManager.mContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isUserCoordinatesNull():Boolean{
        return (Prefs.userCoordinates.latitude == 0.0 && Prefs.userCoordinates.longitude == 0.0)
    }

}