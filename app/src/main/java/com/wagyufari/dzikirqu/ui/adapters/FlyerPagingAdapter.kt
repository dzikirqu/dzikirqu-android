package com.wagyufari.dzikirqu.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.wagyufari.dzikirqu.base.BaseViewHolder
import com.wagyufari.dzikirqu.databinding.*
import com.wagyufari.dzikirqu.model.Flyer
import com.wagyufari.dzikirqu.util.ViewUtils.dpToPx


class FlyerPagingAdapter : PagingDataAdapter<Flyer, BaseViewHolder>(FlyerDiff) {

    private var mListener: Callback? = null
    private var on_attach = true


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

    override fun onAttachedToRecyclerView(@NonNull recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class FlyerViewHolder(private val mBinding: ItemFlyerBinding) :
        BaseViewHolder(mBinding.root) {

        override fun onBind(position: Int) {
            val flyer = getItem(position)
            if (position == 0) {
                val params = mBinding.root.layoutParams as RecyclerView.LayoutParams
                params.setMargins(dpToPx(16), dpToPx(8), dpToPx(8), dpToPx(8))
                mBinding.root.layoutParams = params
            }
            Glide.with(mBinding.image).load(flyer?.image)
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