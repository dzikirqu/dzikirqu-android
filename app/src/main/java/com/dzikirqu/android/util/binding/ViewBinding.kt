package com.dzikirqu.android.util.binding

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.slider.Slider
import com.dzikirqu.android.util.ViewUtils.scale

object ViewBinding {

    @InverseBindingAdapter(attribute = "android:value")
    fun getSliderValue(slider: Slider) = slider.value

    @BindingAdapter("android:valueAttrChanged")
    fun setSliderListeners(slider: Slider, attrChange: InverseBindingListener) {
        slider.addOnChangeListener { _, _, _ ->
            attrChange.onChange()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("onClickAnimate")
    @JvmStatic
    fun onClickAnimate(view: View, runnable: ()->Unit){
        view.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    view.scale(0.8f,duration = 300)
                }
                MotionEvent.ACTION_UP->{
                    view.scale(1f, duration = 300)
                    runnable.invoke()
                }
                MotionEvent.ACTION_CANCEL->{
                    view.scale(1f, duration = 300)
                }
            }
            true
        }
    }
    fun View.onClickAnimate(view: View, runnable: ()->Unit){
        view.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    view.scale(0.8f,duration = 300)
                }
                MotionEvent.ACTION_UP->{
                    view.scale(1f, duration = 300)
                    runnable.invoke()
                }
                MotionEvent.ACTION_CANCEL->{
                    view.scale(1f, duration = 300)
                }
            }
            true
        }
    }
}