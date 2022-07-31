package com.wagyufari.dzikirqu.model.events

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