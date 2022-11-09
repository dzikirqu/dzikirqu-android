package com.dzikirqu.android.model.events

class MainTabEvent(val type:MainTabType)

enum class MainTabType(val id:Int){
    HOME(0),
    QURAN(1),
    PRAYER(2),
}