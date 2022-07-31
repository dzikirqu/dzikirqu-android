package com.wagyufari.dzikirqu.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.databinding.ItemComposeBinding


class ComposeAdapter<T>(val mListener:(data:T, composeView: ComposeView, position:Int)->Unit): RecyclerView.Adapter<BaseViewHolder>(){

    private var dataSet = mutableListOf<T>()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ComposeViewHolder(ItemComposeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    fun submitList(dataSet:List<T>){
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    inner class ComposeViewHolder(private val mBinding: ItemComposeBinding) :
        BaseViewHolder(mBinding.root) {

        @SuppressLint("CheckResult")
        override fun onBind(position: Int) {
            mListener(dataSet[position], mBinding.composeView, position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }
}

