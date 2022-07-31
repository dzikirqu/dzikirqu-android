package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.*
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.model.jump.JumpQuranModel
import com.wagyufari.dzikirqu.util.StringExt.getText


class JumpQuranAdapter : ListAdapter<JumpQuranModel, BaseViewHolder>(SearchQuranDiff) {

    private var mListener: Callback? = null

    object SearchQuranDiff : DiffUtil.ItemCallback<JumpQuranModel>() {
        override fun areItemsTheSame(
            oldItem: JumpQuranModel,
            newItem: JumpQuranModel
        ): Boolean {
            return oldItem.surah.id == oldItem.surah.id
        }

        override fun areContentsTheSame(
            oldItem: JumpQuranModel,
            newItem: JumpQuranModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(ItemJumpQuranBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(surah: Surah, ayah:Int)
    }

    inner class ViewHolder(private val mBinding: ItemJumpQuranBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val result = getItem(position)
            mBinding.title.text = String.format(LocaleConstants.N_AYAH_N.locale(), result.surah.name, result.verse)
            mBinding.subtitle.text = result.surah.translation.getText()
            mBinding.root.setOnClickListener {
                mListener?.onSelectedItem(result.surah, result.verse)
            }
        }

    }
}