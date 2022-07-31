package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.databinding.ItemPrayerGridBinding
import com.wagyufari.dzikirqu.ui.main.prayer.PrayerType
import com.wagyufari.dzikirqu.util.StringExt.getText

class PrayerTypeGridAdapter : ListAdapter<PrayerType, BaseViewHolder>(PrayerDiff) {

    private var mListener: Callback? = null

    object PrayerDiff : DiffUtil.ItemCallback<PrayerType>() {
        override fun areItemsTheSame(oldItem: PrayerType, newItem: PrayerType): Boolean {
            return oldItem.title.toString() == newItem.title.toString()
        }

        override fun areContentsTheSame(oldItem: PrayerType, newItem: PrayerType): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return PrayerTypeGridViewHolder(
            ItemPrayerGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(prayer: PrayerType)
    }

    inner class PrayerTypeGridViewHolder(private val mBinding: ItemPrayerGridBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            mBinding.title.text = getItem(position)?.title?.getText()
            mBinding.image.setImageDrawable(getItem(position)?.image?.let {
                ContextCompat.getDrawable(mBinding.root.context,
                    it)
            })
            mBinding.image.setOnClickListener{
                mListener?.onSelectedItem(getItem(position))
            }
        }
    }
}