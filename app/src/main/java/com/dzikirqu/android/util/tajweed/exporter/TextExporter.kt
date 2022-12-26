package com.dzikirqu.android.util.tajweed.exporter

import com.dzikirqu.android.util.tajweed.model.Result

class TextExporter : Exporter {

    override fun export(ayah: String, results: List<Result>, stringResult: (String) -> Unit) {
        println("Considering: $ayah")
        for (result in results) {
            println(
                "matched " + result.resultType.debugName +
                        " at " + result.start + " to " + result.ending
            )
        }
    }
}