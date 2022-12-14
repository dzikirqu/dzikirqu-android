package com.wagyufari.dzikirqu.util.binding

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.wagyufari.dzikirqu.model.PrayerData
import com.wagyufari.dzikirqu.ui.adapters.JuzAdapter
import com.wagyufari.dzikirqu.ui.adapters.PrayerReadAdapter

object AdapterBinding {

    @BindingAdapter("readPrayerData")
    @JvmStatic
    fun setReadPrayerData(recyclerView: ViewPager2, items: LiveData<List<PrayerData>>) {
        val adapter = recyclerView.adapter as PrayerReadAdapter?
        if (adapter != null) {
            items.value?.let {
                adapter.clearItems()
                adapter.addItems(it.toCollection(arrayListOf()))
            }
        }
    }
    @BindingAdapter("juzAdapter")
    @JvmStatic
    fun setJuzAdapterData(recyclerView: RecyclerView, items: LiveData<List<Any>>) {
        val adapter = recyclerView.adapter as JuzAdapter?
        if (adapter != null) {
            items.value?.let {
                adapter.clearItems()
                adapter.addItems(it.toCollection(arrayListOf()))
            }
        }
    }
}