package com.dzikirqu.android.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dzikirqu.android.data.room.BookmarkDatabase
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.databinding.ItemEmptyBinding

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)

    val mContext: Context = itemView.context
    val mBookmarkDao = BookmarkDatabase.getDatabase(itemView.context).bookmarkDao()
    val mSurahDao = PersistenceDatabase.getDatabase(itemView.context).surahDao()

    class EmptyViewHolder(private val mBinding: ItemEmptyBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {

        }
    }
}

abstract class BaseEventViewHolder(itemView:View): BaseViewHolder(itemView){
    abstract fun onEvent(obj:Any)

    class EmptyViewHolder(private val mBinding: ItemEmptyBinding) :
        BaseEventViewHolder(mBinding.root) {
        override fun onEvent(obj: Any) {

        }

        override fun onBind(position: Int) {

        }
    }
}
