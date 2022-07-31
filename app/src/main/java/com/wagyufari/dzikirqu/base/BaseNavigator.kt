package com.wagyufari.dzikirqu.base

import androidx.core.graphics.Insets

interface BaseNavigator {
    fun finish()
    fun toast(message:String?)
    fun showLoading()
    fun hideLoading()
    fun onSettingsEvent()
    fun onApplyWindowEvent(insets:Insets)
}