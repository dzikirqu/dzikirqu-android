package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.databinding.ItemHadithBookBinding
import com.wagyufari.dzikirqu.model.Hadith
import com.wagyufari.dzikirqu.util.StringExt.getText


class HadithBookAdapter : ListAdapter<Hadith, BaseViewHolder>(BookDiff) {

    private var mListener: Callback? = null

    object BookDiff : DiffUtil.ItemCallback<Hadith>() {
        override fun areItemsTheSame(oldItem: Hadith, newItem: Hadith): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hadith, newItem: Hadith): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return HadithViewHolder(
            ItemHadithBookBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(hadith: Hadith, view: View)
    }

    inner class HadithViewHolder(private val mBinding: ItemHadithBookBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val hadith = getItem(position)
            mBinding.name.text = hadith.name
            mBinding.count.text = hadith.description.getText()
            mBinding.root.setOnClickListener {
                mBinding.card.transitionName = hadith.name
                mListener?.onSelectedItem(hadith, mBinding.card)
            }
        }
    }
}