package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.databinding.ItemSearchQuranBinding
import com.dzikirqu.android.model.Ayah
import com.dzikirqu.android.model.search.SearchQuranModel
import com.dzikirqu.android.util.LocaleProvider
import com.dzikirqu.android.util.ViewUtils.isHidden
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main


class SearchQuranAdapter : ListAdapter<SearchQuranModel, BaseViewHolder>(SearchQuranDiff) {

    private var mListener: Callback? = null

    object SearchQuranDiff : DiffUtil.ItemCallback<SearchQuranModel>() {
        override fun areItemsTheSame(
            oldItem: SearchQuranModel,
            newItem: SearchQuranModel
        ): Boolean {
            return oldItem.surah == newItem.surah
        }

        override fun areContentsTheSame(
            oldItem: SearchQuranModel,
            newItem: SearchQuranModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(ItemSearchQuranBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    var isLoaded = false

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(ayah: Ayah)
    }

    var query = ""

    inner class ViewHolder(private val mBinding: ItemSearchQuranBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val result = getItem(position)

            var isExpanded = false
            mBinding.ayahContainer.isHidden(true)
            mBinding.root.setOnClickListener {
                mBinding.ayahContainer.isHidden(!mBinding.ayahContainer.isHidden())
                mBinding.arrow.setImageResource(if (isExpanded) R.drawable.ic_keyboard_arrow_down else R.drawable.ic_keyboard_arrow_up)
                isExpanded = !isExpanded
                configureAyahAdapter(result.ayah)
            }
            mContext.io{
                mSurahDao.getSurahById(result.surah).firstOrNull()?.let { surah->
                    mContext.main{
                        mBinding.surah.text = surah.name
                        mBinding.found.text = String.format(LocaleProvider.getString(LocaleConstants.N_AYAH_FOUND), result.ayah.count())
                    }
                }
            }
        }

        fun configureAyahAdapter(ayah:List<Ayah>){
            mBinding.ayahContainer.apply{
                adapter = QuranListedAdapter().apply {
                    isSearchMode(query)
                    addItems(ayah.toCollection(arrayListOf()))
                    setListener(object:QuranListedAdapter.Callback{
                        override fun onSelectedItem(ayah: Ayah) {
                            mListener?.onSelectedItem(ayah)
                        }
                    })
                }
                layoutManager = LinearLayoutManager(mContext)
            }
        }
    }
}