package com.wagyufari.dzikirqu.ui.adapters

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.room.dao.getAyahLineDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.ItemKhatamIterationBinding
import com.wagyufari.dzikirqu.model.KhatamStateConstants
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.util.io
import com.wagyufari.dzikirqu.util.main


class KhatamIterationAdapter : ListAdapter<QuranLastRead, BaseViewHolder>(BookmarkDiff) {

    private var mListener: Callback? = null
    private var isInactive: Boolean = false

    object BookmarkDiff : DiffUtil.ItemCallback<QuranLastRead>() {
        override fun areItemsTheSame(oldItem: QuranLastRead, newItem: QuranLastRead): Boolean {
            return oldItem.state == newItem.state
        }

        override fun areContentsTheSame(oldItem: QuranLastRead, newItem: QuranLastRead): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return KhatamIterationViewHolder(
            ItemKhatamIterationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    interface Callback {
        fun onSelectIteration(lastRead: QuranLastRead)
        fun onClickRead(lastRead:QuranLastRead)
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    fun isInactive(isInactive:Boolean){
        this.isInactive = isInactive
    }

    inner class KhatamIterationViewHolder(val mBinding: ItemKhatamIterationBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            val lastRead = getItem(position)
            mBinding.root.context.apply {
                io {
                    mBinding.apply {
                        val value =
                            getAyahLineDao().getAyahLineByKey("${lastRead.surah}:${lastRead.ayah}")
                                .firstOrNull()?.page
                                ?: 1
                        val surahName =
                            getSurahDao().getSurahById(lastRead.surah).firstOrNull()?.name
                        val maxValue = 604
                        val isActive = lastRead.state == KhatamStateConstants.ACTIVE
                        main {

                            read.isVisible = isActive
                            read.setOnClickListener {
                                mListener?.onClickRead(lastRead)
                            }
                            read.text = LocaleConstants.READ_NOW.locale()

                            textIndex.text = "Khattam #${lastRead.lap}"
                            textIndex.setTextColor(
                                if (isActive && !isInactive) resources.getColor(R.color.textPrimary) else resources.getColor(
                                    R.color.textTertiary
                                )
                            )

                            textState.text =
                                if (isActive && !isInactive) LocaleConstants.ACTIVE.locale() else LocaleConstants.INACTIVE.locale()
                            textState.setTextColor(
                                if (!isInactive && isActive) resources.getColor(R.color.white) else resources.getColor(
                                    R.color.textTertiary
                                )
                            )
                            textState.backgroundTintList =
                                if (isActive && !isInactive) resources.getColorStateList(R.color.colorPrimary) else resources.getColorStateList(
                                    R.color.neutral_300
                                )
                            textLastReadTitle.setTextColor(
                                if (isActive && !isInactive) resources.getColor(R.color.textPrimary) else resources.getColor(
                                    R.color.textTertiary
                                )
                            )

                            textLastReadTitle.text = LocaleConstants.LAST_READ_COLON.locale()

                            textProgress.text = LocaleConstants.PAGE.locale() + " " + String.format(LocaleConstants.OF.locale(), value, maxValue)

                            progress.max = maxValue
                            ValueAnimator.ofInt(0, value).apply {
                                this.duration = 1000
                                addUpdateListener {
                                    val value = it.animatedValue as Int
                                    progress.progress = if (isActive) value else 0
                                    progress.secondaryProgress = if (!isActive) value else 0
                                    textProgressPercentage.text =
                                        "${value?.times(100)?.div(maxValue)}%"
                                }
                                start()
                            }
                            textLastRead.text = "$surahName ${lastRead.ayah}"
                            card.setOnClickListener {
                                if (!isInactive){
                                    mListener?.onSelectIteration(getItem(position))
                                }
                            }
                        }
                    }

                }
            }
        }
    }

}