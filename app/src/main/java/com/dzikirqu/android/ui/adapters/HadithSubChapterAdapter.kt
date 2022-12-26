package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.databinding.ItemHadithSubChapterBinding
import com.dzikirqu.android.model.HadithSubChapter
import com.dzikirqu.android.util.StringExt.getText


class HadithSubChapterAdapter : ListAdapter<HadithSubChapter, BaseViewHolder>(BookDiff) {

    private var mListener: Callback? = null

    object BookDiff : DiffUtil.ItemCallback<HadithSubChapter>() {
        override fun areItemsTheSame(oldItem: HadithSubChapter, newItem: HadithSubChapter): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HadithSubChapter, newItem: HadithSubChapter): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return HadithViewHolder(
            ItemHadithSubChapterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(subChapter: HadithSubChapter)
    }

    inner class HadithViewHolder(private val mBinding: ItemHadithSubChapterBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val subchapter = getItem(position)
            mBinding.chapter.text = "${LocaleConstants.CHAPTER.locale()} ${subchapter.chapterIndex}"
            mBinding.title.text = "${subchapter.title.getText()}"
            mBinding.root.setOnClickListener {
                mListener?.onSelectedItem(subchapter)
            }
        }
    }
}