package com.wagyufari.dzikirqu.data.alqurancloud

import com.wagyufari.dzikirqu.constants.Quran
import com.wagyufari.dzikirqu.data.alqurancloud.model.Bitrate
import com.wagyufari.dzikirqu.domain.repository.QuranRecitalRepository
import javax.inject.Inject

class QuranRecitalRepositoryImpl @Inject constructor(
    private val alQuranCloudAPIService: AlQuranCloudAPIService,
) : QuranRecitalRepository {
    override fun getSurahAyahAudioURL(
        bitrate: Int,
        edition: String,
        surah: Int,
        surahAyah: Int,
    ): String {
        val bitrateEnum = enumValues<Bitrate>().find { it.value == bitrate }
            ?: throw IllegalArgumentException("Not such bitrate available")
        val ayah = Quran.getAyahNumber(surah, surahAyah)
        return alQuranCloudAPIService.getAyahRecitalAudioURL(bitrateEnum, edition, ayah)
    }
}