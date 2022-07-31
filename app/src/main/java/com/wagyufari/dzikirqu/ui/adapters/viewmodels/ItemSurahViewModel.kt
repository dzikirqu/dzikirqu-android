package com.wagyufari.dzikirqu.ui.adapters.viewmodels

import androidx.databinding.ObservableField
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.LocaleProvider

class ItemSurahViewModel (val data: Surah){

    fun getRevelation():ObservableField<String>{
        return if(data.revelation == MAKKIYYAH){
            ObservableField("Makkah")
        } else{
            ObservableField("Madinah")
        }
    }

    val verses = ObservableField("${data.verses} ${LocaleProvider.getInstance().getString(LocaleConstants.AYAH)}")
//    val arabic = ObservableField("سورة${data.translation.getArabic()}")
    val translation = ObservableField(data.translation.getText())
    val index = ObservableField("${data.id}")

    companion object{
        val MAKKIYYAH = "makkah"
        val MADANIYYAH = "madinah"
    }
}