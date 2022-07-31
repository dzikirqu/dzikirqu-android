package com.wagyufari.dzikirqu.util.tajweed.exporter

import com.wagyufari.dzikirqu.util.tajweed.model.Result

interface Exporter {
    fun export(ayah: String, results: List<Result>, stringResult:(String) -> Unit)
    fun onOutputStarted() {}
    fun onOutputCompleted() {}
}