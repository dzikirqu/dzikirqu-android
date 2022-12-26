package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.databinding.ItemPrayerBookBinding
import com.dzikirqu.android.model.PrayerBook
import com.dzikirqu.android.util.StringExt.getText


class PrayerBookAdapter : ListAdapter<PrayerBook, BaseViewHolder>(BookDiff) {

    private var mListener: Callback? = null

    object BookDiff : DiffUtil.ItemCallback<PrayerBook>() {
        override fun areItemsTheSame(oldItem: PrayerBook, newItem: PrayerBook): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PrayerBook, newItem: PrayerBook): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewBinding = ItemPrayerBookBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(viewBinding)
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(book: PrayerBook)
    }

    inner class BookViewHolder(private val mBinding: ItemPrayerBookBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val book = getItem(position)
            mBinding.title.text = book.title.getText()
            mBinding.description.text = book.description.getText()
            mBinding.card.setOnClickListener {
                mListener?.onSelectedItem(book)
            }

        }
    }
}