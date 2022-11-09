package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.databinding.*
import com.dzikirqu.android.model.AyahWbw

import com.dzikirqu.android.util.StringExt.getArabic
import com.dzikirqu.android.util.StringExt.getText


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