package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.dzikirqu.android.base.BaseViewHolder
import com.dzikirqu.android.databinding.*
import com.dzikirqu.android.model.Flyer


class FlyerAdapter : ListAdapter<Flyer, BaseViewHolder>(FlyerDiff) {

    private var mListener: Callback? = null

    object FlyerDiff : DiffUtil.ItemCallback<Flyer>() {
        override fun areItemsTheSame(oldItem: Flyer, newItem: Flyer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Flyer, newItem: Flyer): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (isGrid){
            FlyerGridViewHolder(
                ItemFlyerGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else{
            FlyerViewHolder(
                ItemFlyerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    var isGrid = false
    fun asGrid(){
        isGrid = true
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(flyer: Flyer)
    }

    inner class FlyerViewHolder(private val mBinding: ItemFlyerBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val flyer = getItem(position)
            Glide.with(mBinding.image)
                .load(flyer?.image)
                .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mBinding.image)
            mBinding.root.setOnClickListener {
                if (flyer != null) {
                    mListener?.onSelectedItem(flyer)
                }
            }
        }
    }
    inner class FlyerGridViewHolder(private val mBinding: ItemFlyerGridBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val flyer = getItem(position)
            Glide.with(mBinding.image).load(flyer?.image)
                .transition(
                    DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                    )
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mBinding.image)
            mBinding.root.setOnClickListener {
                if (flyer != null) {
                    mListener?.onSelectedItem(flyer)
                }
            }
        }
    }
}