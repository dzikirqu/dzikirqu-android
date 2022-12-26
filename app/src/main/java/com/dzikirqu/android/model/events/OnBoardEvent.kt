package com.dzikirqu.android.model.events

data class OnBoardEvent(val type:OnBoardType)

enum class OnBoardType {
    LANGUAGE,
    LOCATION,
    PRAYTIME,
}