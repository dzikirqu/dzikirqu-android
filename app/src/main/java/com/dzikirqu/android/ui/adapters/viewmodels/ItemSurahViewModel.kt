package com.dzikirqu.android.ui.adapters.viewmodels

import androidx.databinding.ObservableField
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.model.Surah
import com.dzikirqu.android.util.StringExt.getText
import com.dzikirqu.android.util.LocaleProvider

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