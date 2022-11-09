package com.dzikirqu.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.SeparatorItem
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.databinding.ItemSurahBinding
import com.dzikirqu.android.model.Surah
import com.dzikirqu.android.ui.adapters.viewmodels.ItemSurahViewModel
import com.dzikirqu.android.util.FileUtils

class SurahAdapter : ListAdapter<Surah, BaseViewHolder>(
    SurahComparator
) {
    private var mListener: Callback? = null

    companion object {
        private val SurahComparator = object : DiffUtil.ItemCallback<Surah>() {
            override fun areItemsTheSame(oldItem: Surah, newItem: Surah): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Surah, newItem: Surah): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return SurahViewHolder(
            ItemSurahBinding.inflate(
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
        fun onClickSurah(surah: Surah)
        fun onLongClickSurah(surah: Surah)
    }

    inner class SurahViewHolder(private val mBinding: ItemSurahBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            val surah = getItem(position)
            mBinding.root.setOnClickListener { mListener?.onClickSurah(surah) }
            mBinding.root.setOnLongClickListener {
                mListener?.onLongClickSurah(surah)
                true
            }
            mBinding.viewModel = ItemSurahViewModel(surah)
            mBinding.arabic.text = getArabicCalligraphy(mBinding.root.context, surah.id)
        }
    }

    fun getArabicCalligraphy(mContext: Context, surah: Int): String {
        return Gson().fromJson<ArrayList<SeparatorItem>>(
            FileUtils.getJsonStringFromAssets(
                mContext,
                "json/quran/paged/separator.json"
            ), object :
                TypeToken<ArrayList<SeparatorItem>>() {}.type
        ).filter { it.surah == surah }.firstOrNull()?.unicode ?: ""
    }

}