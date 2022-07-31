package com.wagyufari.dzikirqu.util.tajweed.exporter

import com.wagyufari.dzikirqu.util.tajweed.model.Result

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