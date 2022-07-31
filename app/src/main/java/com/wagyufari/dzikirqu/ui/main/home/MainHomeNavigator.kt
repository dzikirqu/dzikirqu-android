package com.wagyufari.dzikirqu.ui.main.home

import com.wagyufari.dzikirqu.base.BaseNavigator

interface MainHomeNavigator:BaseNavigator{
    fun onClickSearch()
    fun onClickBookmark()
    fun onClickSettings()
    fun onClickGrantPermission()
    fun onClickReadQuran()
    fun onClickPraytime()
}