package com.wagyufari.dzikirqu.util

import android.widget.TextView
import com.wagyufari.dzikirqu.util.markwon.AlignTagHandler
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

fun TextView.setMarkdown(text:String){
    Markwon
        .builder(context)
        .usePlugin(HtmlPlugin.create {
            it.addHandler(AlignTagHandler())
        })
        .build().setMarkdown(this, text)
}