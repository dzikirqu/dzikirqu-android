package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.ItemHadithChapterBinding
import com.wagyufari.dzikirqu.model.HadithChapter
import com.wagyufari.dzikirqu.model.HadithSubChapter
import com.wagyufari.dzikirqu.util.StringExt.getText


class HadithChapterAdapter : ListAdapter<HadithChapter, BaseViewHolder>(BookDiff) {

    private var mListener: Callback? = null

    object BookDiff : DiffUtil.ItemCallback<HadithChapter>() {
        override fun areItemsTheSame(oldItem: HadithChapter, newItem: HadithChapter): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HadithChapter, newItem: HadithChapter): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return HadithViewHolder(
            ItemHadithChapterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectChapter(chapter: HadithChapter)
        fun onSelectSubChapter(subchapter: HadithSubChapter)
    }

    inner class HadithViewHolder(private val mBinding: ItemHadithChapterBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val chapter = getItem(position)
            mBinding.title.text = chapter.title.getText()
            mBinding.index.text =
                "${LocaleConstants.HADITH.locale()} ${chapter.startIndex} - ${LocaleConstants.HADITH.locale()} ${chapter.endIndex}"
            mBinding.recycler.apply {
                layoutManager = LinearLayoutManager(mBinding.root.context)
                adapter = HadithSubChapterAdapter().apply {
                    submitList(chapter.chapters)
                    setListener(object : HadithSubChapterAdapter.Callback {
                        override fun onSelectedItem(subChapter: HadithSubChapter) {
                            mListener?.onSelectSubChapter(subChapter)
                        }
                    })
                }
            }

            mBinding.clickable.setOnClickListener {
                mListener?.onSelectChapter(chapter)
            }
            mBinding.chevron.setOnClickListener {
                mBinding.recycler.isVisible = !mBinding.recycler.isVisible
                mBinding.chevron.scaleY = if (mBinding.recycler.isVisible) -1f else 1f
            }
        }
    }
}