package com.dzikirqu.android.util.tajweed.exporter

import com.dzikirqu.android.util.tajweed.model.Result

interface Exporter {
    fun export(ayah: String, results: List<Result>, stringResult:(String) -> Unit)
    fun onOutputStarted() {}
    fun onOutputCompleted() {}
}