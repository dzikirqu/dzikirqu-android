package com.wagyufari.dzikirqu.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.model.Bookmark
import com.wagyufari.dzikirqu.model.Note
import com.wagyufari.dzikirqu.model.Prayer

class GenericAdapter<D:Any, B: ViewBinding>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B, val mListener1:((data:D, binding: B)->Unit)?={ a, b->}, val mListener2:((data:D, binding: B, position:Int)->Unit)?={ a, b, c->}): ListAdapter<D, BaseViewHolder>(BaseItemCallback<D>()){

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return GenericViewHolder(bindingInflater(LayoutInflater.from(parent.context), parent, false))
    }

    inner class GenericViewHolder(private val mBinding: B) :
        BaseViewHolder(mBinding.root) {

        @SuppressLint("CheckResult")
        override fun onBind(position: Int) {
            mListener1?.invoke(getItem(position), mBinding)
            mListener2?.invoke(getItem(position), mBinding, position)
        }
    }
}

class BaseItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        if (oldItem is Note && newItem is Note){
            return oldItem.id == newItem.id
        } else if (oldItem is Prayer && newItem is Prayer){
            return oldItem.id == newItem.id
        } else if (oldItem is Bookmark && newItem is Bookmark){
            return oldItem.id == newItem.id
        }
        return oldItem.toString() == newItem.toString()
    }
}