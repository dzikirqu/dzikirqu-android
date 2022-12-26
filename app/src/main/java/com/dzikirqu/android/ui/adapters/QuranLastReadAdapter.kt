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
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.repository.SurahRepository
import com.dzikirqu.android.databinding.ItemQuranLastReadAttachmentBinding
import com.dzikirqu.android.databinding.ItemQuranLastReadBinding
import com.dzikirqu.android.model.QuranLastRead
import com.dzikirqu.android.model.QuranLastReadString
import com.dzikirqu.android.util.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class QuranLastReadAdapter : ListAdapter<QuranLastRead, BaseViewHolder>(
    QuranLastReadComparator
) {
    private var mListener: Callback? = null

    companion object {
        private val QuranLastReadComparator = object : DiffUtil.ItemCallback<QuranLastRead>() {
            override fun areItemsTheSame(oldItem: QuranLastRead, newItem: QuranLastRead): Boolean {
                return (oldItem.surah == newItem.surah) && (oldItem.ayah == newItem.ayah)
            }

            override fun areContentsTheSame(
                oldItem: QuranLastRead,
                newItem: QuranLastRead
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (Prefs.quranLastRead == getItem(position)) {
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == 0) {
            QuranLastReadAttachmentViewHolder(
                ItemQuranLastReadAttachmentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            QuranLastReadViewHolder(
                ItemQuranLastReadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    override fun onCurrentListChanged(
        previousList: MutableList<QuranLastRead>,
        currentList: MutableList<QuranLastRead>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        mListener?.onCurrentListChanged()
    }

    interface Callback {
        fun onClickLastRead(lastRead: QuranLastRead)
        fun onLongClickLastRead(lastRead: QuranLastRead)
        fun onCurrentListChanged()
    }

    inner class QuranLastReadViewHolder(private val mBinding: ItemQuranLastReadBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            CoroutineScope(Main).launch {
                val lastRead = getItem(position).getLastRead()
                mBinding.surahName.text = lastRead.surah
                mBinding.ayah.text = lastRead.ayah
                mBinding.unicode.text = lastRead.unicode
                mBinding.root.setOnClickListener {
                    mListener?.onClickLastRead(getItem(position))
                }
                mBinding.root.setOnLongClickListener {
                    mListener?.onLongClickLastRead(getItem(position))
                    true
                }
            }
        }

        suspend fun QuranLastRead.getLastRead(): QuranLastReadString {
            val surah = SurahRepository.getInstance(mBinding.root.context).getSurahById(surah)
            return QuranLastReadString(
                surah?.name ?: "",
                if (isSavedFromPage == true) "${LocaleConstants.PAGE.locale()} $page" else "${LocaleConstants.AYAH.locale()} $ayah",
                getArabicCalligraphy(mBinding.root.context, this.surah)
            )
        }
    }

    inner class QuranLastReadAttachmentViewHolder(private val mBinding: ItemQuranLastReadAttachmentBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            CoroutineScope(Main).launch {
                val lastRead = getItem(position).getLastRead()
                mBinding.surahName.text = lastRead.surah
                mBinding.ayah.text = lastRead.ayah
                mBinding.unicode.text = lastRead.unicode
                mBinding.root.setOnClickListener {
                    mListener?.onClickLastRead(getItem(position))
                }
                mBinding.root.setOnLongClickListener {
                    mListener?.onLongClickLastRead(getItem(position))
                    true
                }
            }
        }

        suspend fun QuranLastRead.getLastRead(): QuranLastReadString {
            val surah = SurahRepository.getInstance(mBinding.root.context).getSurahById(surah)
            return QuranLastReadString(
                surah?.name ?: "",
                "${LocaleConstants.AYAH.locale()} $ayah",
                getArabicCalligraphy(mBinding.root.context, this.surah)
            )
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