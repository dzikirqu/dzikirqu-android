package com.wagyufari.dzikirqu.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.gojekuiclone.util.ext.bindView


@SuppressLint("ResourceAsColor")
class TitleSubtitleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    val title: TextView by bindView(R.id.title)
    val subtitle: TextView by bindView(R.id.subtitle)
    val drawableEnd: ImageView by bindView(R.id.drawableEnd)

    init {
        inflate(context, R.layout.title_subtitle_view, this)

        val array = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TitleSubtitleView,
            0, 0)
        try {
            title.text = array.getString(R.styleable.TitleSubtitleView_title)?.locale()
            subtitle.text = array.getString(R.styleable.TitleSubtitleView_subtitle)?.locale()
            title.setTextColor(array.getColor(R.styleable.TitleSubtitleView_titleTextColor, R.color.textPrimary))
            subtitle.setTextColor(array.getColor(R.styleable.TitleSubtitleView_subtitleTextColor, R.color.textTertiary))
            drawableEnd.setImageResource(array.getResourceId(R.styleable.TitleSubtitleView_drawableEnd, R.drawable.ic_dzikir))
            drawableEnd.isVisible = array.getResourceId(R.styleable.TitleSubtitleView_drawableEnd, R.drawable.ic_dzikir) != R.drawable.ic_dzikir
            drawableEnd.setColorFilter(array.getColor(R.styleable.TitleSubtitleView_drawableEndTint, R.color.neutral_400), android.graphics.PorterDuff.Mode.SRC_IN)
        } finally {
            array.recycle()
        }
    }

    fun setTitle(title:String){
        this.title.text = title.locale()
    }
    fun setSubtitle(subtitle:String){
        this.subtitle.text = subtitle.locale()
    }

    fun delay(delay: Long, runnable: () -> Unit) {
        Handler().postDelayed({
            runnable.invoke()
        }, delay)
    }

}