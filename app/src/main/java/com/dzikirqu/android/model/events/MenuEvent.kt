package com.dzikirqu.android.model.events

class MenuEvent(var type:MenuEventType?=null){
    companion object{
        var LastRead = MenuEvent(MenuEventType.LastRead)
        var AutoLastRead = MenuEvent(MenuEventType.AutoLastRead)
    }
}

enum class MenuEventType{
    LastRead,
    AutoLastRead
}