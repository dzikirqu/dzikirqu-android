package com.wagyufari.dzikirqu.util.binding

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.switchmaterial.SwitchMaterial
import com.wagyufari.dzikirqu.model.LanguageString
import com.wagyufari.dzikirqu.model.PrayerTime
import com.wagyufari.dzikirqu.util.LocaleProvider
import com.wagyufari.dzikirqu.util.StringExt.getText

object TextViewBinding {

    @BindingAdapter("maxLines")
    @JvmStatic
    fun setMaxlines(view: TextView, maxlines: Int) {
        view.post {
            if (view.text != null) {
                view.maxLines = maxlines
            }
            Int.MAX_VALUE
        }
    }

    @BindingAdapter("typefaceQuran")
    @JvmStatic
    fun setTypefaceQuran(view:TextView, page:Int){
        view.typeface = Typeface.createFromAsset(view.context.assets, "fonts/quran/p$page.ttf")
    }


    @BindingAdapter("text")
    @JvmStatic
    fun setText(view:TextView, string:ArrayList<LanguageString>?){
        view.text = string?.getText()
    }

    @BindingAdapter("maxLines")
    @JvmStatic
    fun setMaxLines(view:TextView, maxLines:Int){
        view.maxLines = maxLines
        view.setOnClickListener {
            if (view.maxLines == maxLines){
                ValueAnimator.ofInt(maxLines, 70).apply {
                    duration = 800
                    addUpdateListener {
                        view.maxLines = it.animatedValue as Int
                    }
                    start()
                }

            } else{
                ValueAnimator.ofInt(70, maxLines).apply {
                    duration = 800
                    addUpdateListener {
                        view.maxLines = it.animatedValue as Int
                    }
                    start()
                }
            }
        }
    }

    @BindingAdapter("nameAbbreviation")
    @JvmStatic
    fun setNameAbbreviation(view: TextView, _name: String) {
        if (_name.split(" ").size > 1) {
            view.text = "${_name.split(" ")[0][0]}${_name.split(" ")[1][0]}"
        } else {
            view.text = "${_name.split(" ")[0][0]}"
        }
    }


    @BindingAdapter("locale")
    @JvmStatic
    fun bindTextView(view: TextView, str: String) {
        view.text = LocaleProvider.getInstance().getString(str)
    }
    @BindingAdapter("locale")
    @JvmStatic
    fun bindTextView(view: SwitchMaterial, str: String) {
        view.text = LocaleProvider.getInstance().getString(str)
    }

    @BindingAdapter("hintLocale")
    @JvmStatic
    fun bindHint(view:EditText, str:String){
        view.hint = LocaleProvider.getInstance().getString(str)
    }

    @BindingAdapter("textPrayerTime")
    @JvmStatic
    fun bindTextPrayerTime(view:TextView, prayer: PrayerTime){

    }

}