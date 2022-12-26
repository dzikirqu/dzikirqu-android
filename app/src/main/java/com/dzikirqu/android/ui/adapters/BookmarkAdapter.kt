package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.data.room.dao.getAyahDao
import com.dzikirqu.android.databinding.ItemBookmarkPrayerBinding
import com.dzikirqu.android.databinding.ItemBookmarkQuranBinding
import com.dzikirqu.android.databinding.ItemPinnedPrayerBinding
import com.dzikirqu.android.model.Bookmark
import com.dzikirqu.android.model.BookmarkHighlightUpdate
import com.dzikirqu.android.model.BookmarkType
import com.dzikirqu.android.model.Prayer
import com.dzikirqu.android.util.LocaleProvider
import com.dzikirqu.android.util.StringExt.getText
import com.dzikirqu.android.util.ViewUtils.dpToPx
import com.dzikirqu.android.util.binding.ImageViewBinding.setTintColor
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main


class BookmarkAdapter : ListAdapter<Bookmark, BaseViewHolder>(BookmarkDiff) {

    private var mListener: Callback? = null

    companion object {
        const val VIEW_TYPE_QURAN = 1
        const val VIEW_TYPE_PRAYER = 2
    }

    object BookmarkDiff : DiffUtil.ItemCallback<Bookmark>() {
        override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            BookmarkType.QURAN -> VIEW_TYPE_QURAN
            else -> VIEW_TYPE_PRAYER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_QURAN -> {
                val viewBinding =
                    ItemBookmarkQuranBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return BookmarkQuranViewHolder(viewBinding)
            }
            else -> {
                if (isHighlight){
                    val viewBinding =
                        ItemPinnedPrayerBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    return BookmarkPrayerPinnedViewHolder(viewBinding)
                } else{
                    val viewBinding =
                        ItemBookmarkPrayerBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    return BookmarkPrayerViewHolder(viewBinding)
                }
            }
//            VIEW_TYPE_NORMAL -> {
//                val viewBinding = ItemBookHorizontalBinding
//                    .inflate(LayoutInflater.from(parent.context), parent, false)
//                BookHorizontalViewHolder(viewBinding)
//            }
//            VIEW_TYPE_GRID -> {
//                val viewBinding = ItemBookGridBinding
//                    .inflate(LayoutInflater.from(parent.context), parent, false)
//                BookGridViewHolder(viewBinding)
//            }
//            else -> {
//                val viewBinding = ItemEmptyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//                BaseViewHolder.EmptyViewHolder(viewBinding)
//            }
        }

    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    var isHighlight = false
    fun isHighlights() {
        isHighlight = true
    }

    interface Callback {
        fun onSelectQuran(surah: Int, ayah: Int)
        fun onSelectPrayer(prayer: Prayer)
    }

    inner class BookmarkQuranViewHolder(private val mBinding: ItemBookmarkQuranBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val bookmark = getItem(position)
            mBinding.root.context.apply {
                io {
                    val surahDao = PersistenceDatabase.getDatabase(mBinding.root.context).surahDao()
                    val ayah = mBinding.root.context.getAyahDao().getAyahById(bookmark.idString?.toInt()?: 0)
                    val ayahId = ayah?.verse_number
                    val surahId = ayah?.chapterId
                    val surah = surahDao.getSurahById(ayah?.chapterId ?: 0).firstOrNull()
                    main {
                        surah?.let {
                            mBinding.surah.text = it.name
                            mBinding.ayah.text = String.format(
                                LocaleProvider.getString(LocaleConstants.AYAH_N),
                                ayahId
                            )
                        }
                        mBinding.root.setOnClickListener {
                            mListener?.onSelectQuran(surahId ?: 1, ayahId ?: 1)
                        }
                    }
                }
            }
        }
    }

    inner class BookmarkPrayerViewHolder(private val mBinding: ItemBookmarkPrayerBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val bookmark = getItem(position)
            val prayerId = bookmark.idString
            mContext.io {
                val surahDao = PersistenceDatabase.getDatabase(mBinding.root.context).prayerDao()
                val prayer = surahDao.getPrayerByIdSuspend(prayerId.toString()).firstOrNull()
                mContext.main {
                    prayer?.let {
                        mBinding.title.text = it.title?.getText()
                        mBinding.root.setOnClickListener {
                            mListener?.onSelectPrayer(prayer)
                        }
                        configureBookmark(prayer)
                    }
                }
            }
        }

        fun configureBookmark(prayer: Prayer) {
            mContext.io {
                val bookmark = mBookmarkDao.getBookmarkByIdSuspend(prayer.id)
                mBinding.next.setImageResource(if (isHighlight) R.drawable.ic_attachment else R.drawable.ic_bookmarks)
                mBinding.root.setOnClickListener {
                    val popupMenu = PopupMenu(mBinding.root.context, mBinding.root)
                    popupMenu.menu.add(0, 0, 0, "Bookmark")
                    popupMenu.menu.add(0, 0, 0, "Highlight")
                    popupMenu.show()
                }
                mContext.main {
                    mBinding.bookmark.setTintColor(if (bookmark == null) R.color.neutral_600 else R.color.colorPrimary)
                    configureHighlight(bookmark)
                    mBinding.bookmark.setOnClickListener {
                        if (bookmark == null) {
                            mContext.io {
                                mBookmarkDao.putBookmark(
                                    Bookmark(
                                        idString = prayer.id,
                                        type = BookmarkType.PRAYER
                                    )
                                )
                            }
                        } else {
                            mContext.io {
                                mBookmarkDao.deleteBookmark(prayer.id,BookmarkType.PRAYER)
                            }
                        }
                    }
                }
            }
        }

        fun configureHighlight(bookmark: Bookmark?) {
            bookmark?.let {
                mBinding.highlight.setTintColor(if (bookmark.highlighted) R.color.colorPrimary else R.color.neutral_600)
                mBinding.highlight.setOnClickListener {
                    mContext.io {
                        mBookmarkDao.updateHighlight(
                            BookmarkHighlightUpdate(
                                bookmark.id ?: 0,
                                !bookmark.highlighted
                            )
                        )
                    }
                }
            }
        }
    }

    inner class BookmarkPrayerPinnedViewHolder(private val mBinding: ItemPinnedPrayerBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val bookmark = getItem(position)
            mContext.io {
                val surahDao = PersistenceDatabase.getDatabase(mBinding.root.context).prayerDao()
                val prayer = surahDao.getPrayerByIdSuspend(bookmark.idString.toString()).firstOrNull()
                mContext.main {
                    prayer?.let {
                        mBinding.title.text = it.title?.getText()
                        mBinding.root.setOnClickListener {
                            mListener?.onSelectPrayer(prayer)
                        }
                    }
                }
            }

            val params = mBinding.root.layoutParams as RecyclerView.LayoutParams
            if (position == 0) {
                params.setMargins(
                    dpToPx(24),
                    dpToPx(8),
                    dpToPx(8),
                    dpToPx(8)
                )
            }
            mBinding.root.layoutParams = params
        }


    }

}