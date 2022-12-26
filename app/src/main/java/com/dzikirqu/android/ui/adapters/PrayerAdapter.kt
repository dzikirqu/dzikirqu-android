package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.databinding.ItemPrayerBinding
import com.dzikirqu.android.model.Bookmark
import com.dzikirqu.android.model.BookmarkHighlightUpdate
import com.dzikirqu.android.model.BookmarkType
import com.dzikirqu.android.model.Prayer
import com.dzikirqu.android.util.StringExt.getText
import com.dzikirqu.android.util.ViewUtils.isHidden
import com.dzikirqu.android.util.binding.ImageViewBinding.setTintColor
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main

class PrayerAdapter : ListAdapter<Prayer, BaseViewHolder>(PrayerDiff) {

    private var mListener: Callback? = null


    object PrayerDiff : DiffUtil.ItemCallback<Prayer>() {
        override fun areItemsTheSame(oldItem: Prayer, newItem: Prayer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Prayer, newItem: Prayer): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return PrayerViewHolder(
            ItemPrayerBinding.inflate(
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
        fun onSelectedItem(prayer: Prayer)
    }

    private var isSearch = false
    fun isSearch(){
        isSearch = true
    }

    inner class PrayerViewHolder(private val mBinding: ItemPrayerBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val prayer = getItem(position)

            mBinding.index.text = "${position + 1}. "
            mBinding.name.text = prayer.title?.getText()

            mBinding.bookmark.isHidden(isSearch)
            mBinding.root.setOnClickListener {
                mListener?.onSelectedItem(prayer)
            }
            mContext.io{
                configureBookmark(prayer)
            }
        }

        suspend fun configureBookmark(prayer: Prayer) {
            val bookmark = getBookmark(prayer)
            mBinding.bookmark.setTintColor(if (bookmark == null) R.color.neutral_300 else R.color.colorPrimary)
            mContext.main {
                mBinding.bookmark.setOnClickListener {
                    mContext.io{
                        if (bookmark == null){
                            mBookmarkDao.putBookmark(
                                Bookmark(
                                    idString = prayer.id,
                                    type = BookmarkType.PRAYER
                                )
                            )
                        } else{
                            mBookmarkDao.deleteBookmark(prayer.id,BookmarkType.PRAYER)
                        }
                        configureBookmark(prayer)
                    }
                }
                mBinding.highlight.isVisible = bookmark != null
                bookmark?.let {
                    mBinding.highlight.setTintColor(if (bookmark.highlighted) R.color.colorPrimary else R.color.neutral_300)
                    mBinding.highlight.setOnClickListener {
                        mContext.io{
                            mBookmarkDao.updateHighlight(BookmarkHighlightUpdate(bookmark.id ?: 0,
                                !bookmark.highlighted))
                        }
                    }
                }
            }
        }

        suspend fun getBookmark(prayer:Prayer): Bookmark?{
            return mBookmarkDao.getBookmarkByIdSuspend(prayer.id)
        }

    }
}