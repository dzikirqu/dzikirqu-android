package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.ItemHizbBinding
import com.wagyufari.dzikirqu.databinding.ItemJuzBinding
import com.wagyufari.dzikirqu.model.Hizb
import com.wagyufari.dzikirqu.model.Juz

class JuzAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val data: MutableList<Any>
    private var mListener: Callback? = null

    init {
        this.data = ArrayList()
    }

    companion object {
        const val VIEW_TYPE_JUZ = 1
        const val VIEW_TYPE_HIZB = 2
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position] is Hizb) {
            return VIEW_TYPE_HIZB
        } else if (data[position] is Juz) {
            return VIEW_TYPE_JUZ
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_HIZB -> {
                HizbViewHolder(
                    ItemHizbBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                JuzViewHolder(
                    ItemJuzBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }


    fun addItems(data: ArrayList<Any>?) {
        this.data.addAll(data ?: ArrayList())
        notifyDataSetChanged()
    }

    fun clearItems() {
        data.clear()
        notifyDataSetChanged()
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectJuz(juz: Juz)
        fun onSelectHizb(hizb: Hizb)
        fun onTapExpand(juz: Juz)
    }

    inner class JuzViewHolder(private val mBinding: ItemJuzBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            if (data.isNotEmpty()) {
                val juz = data[position] as Juz
                mBinding.index.text = juz.juz.toString()
                mBinding.title.text = "Juz ${juz.juz}"
                mBinding.from.text = String.format(
                    LocaleConstants.STARTS_FROM_N_AYAT_N.locale(),
                    juz.surah,
                    juz.verse
                )
                mBinding.root.setOnClickListener {
                    mListener?.onSelectJuz(juz)
                }
                mBinding.chevron.setImageResource(if (juz.isHizbShown) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down)
                mBinding.chevron.setOnClickListener {
                    mListener?.onTapExpand(if(juz.isHizbShown) juz.also { it.juz = 0 } else juz)
                }
            }
        }
    }

    inner class HizbViewHolder(private val mBinding: ItemHizbBinding) :
        BaseViewHolder(mBinding.root) {
        override fun onBind(position: Int) {
            if (data.isNotEmpty()) {
                val hizb = data[position] as Hizb
                mBinding.hizb.text = "Hizb ${
                    hizb.hizb.toString()
                        .replace(".25", " ¼")
                        .replace(".5", " ½")
                        .replace(".50", " ½")
                        .replace(".75", " ¾")
                        .replace(".0", " ")
                }"
                mBinding.from.text = String.format(
                    LocaleConstants.STARTS_FROM_N_AYAT_N.locale(),
                    hizb.surah,
                    hizb.verse
                )
                mBinding.root.setOnClickListener {
                    mListener?.onSelectHizb(hizb)
                }
            }
        }
    }
}