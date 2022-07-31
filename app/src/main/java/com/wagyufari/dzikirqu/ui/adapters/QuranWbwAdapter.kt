package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.databinding.*
import com.wagyufari.dzikirqu.model.AyahWbw

import com.wagyufari.dzikirqu.util.StringExt.getArabic
import com.wagyufari.dzikirqu.util.StringExt.getText


class QuranWbwAdapter : ListAdapter<AyahWbw, BaseViewHolder>(AyahWbwDiff) {

    object AyahWbwDiff : DiffUtil.ItemCallback<AyahWbw>() {
        override fun areItemsTheSame(
            oldItem: AyahWbw,
            newItem: AyahWbw
        ): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(
            oldItem: AyahWbw,
            newItem: AyahWbw
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemAyahWbwBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val mBinding: ItemAyahWbwBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val word = getItem(position)
            mBinding.arabic.text = word.text.getArabic()
            mBinding.translation.text = word.text.getText()
        }
    }
}