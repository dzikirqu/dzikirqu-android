package com.wagyufari.dzikirqu.domain.repository

interface QuranRecitalRepository {
    fun getSurahAyahAudioURL(bitrate: Int, edition: String, surah: Int, surahAyah: Int): String
}