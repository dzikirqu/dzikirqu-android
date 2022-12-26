package com.dzikirqu.android.util

import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.model.LanguageString
import com.dzikirqu.android.util.tajweed.exporter.Exporter
import com.dzikirqu.android.util.tajweed.exporter.HtmlExporter
import com.dzikirqu.android.util.tajweed.model.Result
import com.dzikirqu.android.util.tajweed.model.TajweedRule
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object StringExt {
    fun ArrayList<LanguageString>.getText():String{
        return filter { it.language == Prefs.language }.firstOrNull()?.text ?: ""
    }

    suspend fun String.getTajweed():String{
        return suspendCoroutine { suspended->
            val exporter: Exporter = HtmlExporter()
            exporter.onOutputStarted()
            val rules = TajweedRule.MADANI_RULES
            val results: ArrayList<Result> = ArrayList()
            for (tajweedRule in rules) {
                results.addAll(tajweedRule.rule.checkAyah(this))
            }
//            ResultUtil.INSTANCE.sort(results)
            exporter.export(this, results) {
                suspended.resume(it)
            }
            exporter.onOutputCompleted()
        }
    }

    fun ArrayList<LanguageString>.getArabic():String{
        return filter { it.language == "arabic" }.firstOrNull()?.text.toString().replace("،","")
    }

    fun Float.hizb():String{
        return "Hizb ${
            this.toString()
                .replace(".25", " ¼")
                .replace(".5", " ½")
                .replace(".50", " ½")
                .replace(".75", " ¾")
                .replace(".0", " ")
        }"
    }
}