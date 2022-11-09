package com.dzikirqu.android.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.room.dao.getNoteDao
import com.dzikirqu.android.databinding.ItemNoteBinding
import com.dzikirqu.android.databinding.ItemNoteFolderBinding
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.ui.note.composer.NoteComposerActivity
import com.dzikirqu.android.util.ViewUtils
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main
import com.dzikirqu.android.util.toAgo


class NoteAdapter : ListAdapter<Any, BaseViewHolder>(NoteStaggeredDiff) {

    private var mListener: Callback? = null

    object NoteStaggeredDiff : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Note && newItem is Note) {
                oldItem.id == newItem.id
            } else {
                oldItem.toString() == newItem.toString()
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Note && newItem is Note) {
                oldItem == newItem
            } else {
                oldItem.toString() == newItem.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Note) 0 else 1
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            0 -> {
                val viewBinding = ItemNoteBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NoteListViewHolder(viewBinding)
            }
            else -> {
                val viewBinding = ItemNoteFolderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                NoteFolderViewHolder(viewBinding)
            }
        }
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectFolder(folder: String, extras: FragmentNavigator.Extras)
    }

    inner class NoteFolderViewHolder(private val mBinding: ItemNoteFolderBinding) :
        BaseViewHolder(mBinding.root) {

        @SuppressLint("CheckResult")
        override fun onBind(position: Int) {

            if (position == 0) {
                val params = mBinding.root.layoutParams as RecyclerView.LayoutParams
                if (position == 0) {
                    params.setMargins(
                        ViewUtils.dpToPx(12),
                        ViewUtils.dpToPx(20),
                        ViewUtils.dpToPx(12),
                        ViewUtils.dpToPx(8)
                    )
                }
            }

            mBinding.name.text = getItem(position) as String
            mBinding.root.context.io {
                val count = mBinding.root.context.getNoteDao()
                    .getNoteByFolderSuspend(getItem(position) as String)
                mBinding.root.context.main {
                    mBinding.count.text =
                        String.format(LocaleConstants.N_NOTES.locale(), count.count())
                }
            }

            val folderName = getItem(position) as String
            ViewCompat.setTransitionName(mBinding.card, folderName)

            mBinding.clickable.setOnClickListener {
                mBinding.root.context.apply {
                    mListener?.onSelectFolder(folderName,
                        FragmentNavigatorExtras(mBinding.card to folderName))
                }
            }
        }
    }

    inner class NoteListViewHolder(private val mBinding: ItemNoteBinding) :
        BaseViewHolder(mBinding.root) {

        @SuppressLint("CheckResult")
        override fun onBind(position: Int) {
            val data = getItem(position) as Note

            if (position == 0) {
                val params = mBinding.root.layoutParams as RecyclerView.LayoutParams
                if (position == 0) {
                    params.setMargins(
                        ViewUtils.dpToPx(12),
                        ViewUtils.dpToPx(20),
                        ViewUtils.dpToPx(12),
                        ViewUtils.dpToPx(8)
                    )
                }
            }

            mBinding.title.text =
                if (data.title?.isNotBlank() == true) data.title else "Untitled note"

            mBinding.title.setTextColor(if (data.title?.isNotBlank() == true) ContextCompat.getColor(
                mBinding.root.context,
                R.color.textPrimary) else ContextCompat.getColor(mBinding.root.context,
                R.color.textTertiary))
            mBinding.subtitle.isVisible = data.subtitle?.isNotBlank() == true
            mBinding.text.isVisible = data.content?.isNotBlank() == true
            mBinding.subtitle.text = data.subtitle
            mBinding.clickable.setOnClickListener {
                mBinding.root.context.apply {
                    startActivity(NoteComposerActivity.newIntent(this,
                        data.id,
                        false))
                }
            }
            mBinding.date.text = data.getUpdatedDateObject()?.toAgo()
            mBinding.text.text = data.content?.replace("\n", " ")
        }
    }
}

data class MultiViewItem (val id : Int, val content : Any?)