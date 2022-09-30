package com.wagyufari.dzikirqu.data.alqurancloud

import com.wagyufari.dzikirqu.constants.Quran
import com.wagyufari.dzikirqu.data.alqurancloud.model.Bitrate

interface AlQuranCloudAPIService {
    fun getAyahRecitalAudioURL(bitrate: Bitrate, edition: String, ayahNumber: Int): String {
        val isEditionExists = Const.AUDIO_BITRATES[bitrate.ordinal]!!.any { it == edition }
        val isAyahInRange = ayahNumber !in 1..Quran.TOTAL_AYAH
        if (!isAyahInRange) {
            throw IllegalArgumentException("Ayah is not exists")
        }
        if (!isEditionExists) {
            throw IllegalArgumentException("Audio edition is not exists")
        }
        return "quran/audio/${bitrate.value}/${edition}/${ayahNumber}.mp3"
    }
}
