package com.dzikirqu.android.util.simplerecycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class SimpleRecyclerViewHolder<V : ViewDataBinding?>(val binding: V) : RecyclerView.ViewHolder(binding?.root!!)