package com.wagyufari.dzikirqu.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.wagyufari.dzikirqu.data.Prefs


class LocaleProvider(private var context: Context?) {


    private var mStringMap: HashMap<String, String>? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ins: LocaleProvider? = null

        @Synchronized
        fun getInstance(): LocaleProvider {
            return ins!!
        }

        @Synchronized
        fun getString(key:String):String{
            return ins!!.getString(key)
        }

        fun init(context: Context) {
            ins = LocaleProvider(context)
        }
    }

    fun getString(key: String): String {
        val language = Prefs.language
        val jsonString = context?.resources?.getIdentifier("locale_$language", "raw", context?.packageName)?.let {
            context?.resources?.openRawResource(it)?.bufferedReader().use { it?.readText() }
        }
        val jsonElement = Gson().fromJson(jsonString, JsonElement::class.java)
        val json = jsonElement.asJsonObject
        for (entry in json.entrySet()) {
            if (entry.key == key) {
                return entry.value.asString
            }
        }
        return key
    }
}
