package com.wagyufari.dzikirqu.util.simplerecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.wagyufari.dzikirqu.BR

class SimpleRecyclerAdapter<T : SimpleRecyclerItem?> :
    RecyclerView.Adapter<SimpleRecyclerViewHolder<*>>() {

    private var data: java.util.ArrayList<out T>? = null
    private var mListener: Callback<T>? = null
    var selected:Int? = 0

    fun setItems(data: java.util.ArrayList<out T>?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return data?.get(position)?.layoutId()!!
    }

    override fun getItemCount(): Int {
        return data?.size?:0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleRecyclerViewHolder<*> {
        val bind = DataBindingUtil.bind<ViewDataBinding>(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
        return SimpleRecyclerViewHolder(bind)
    }

    fun setListener(listener: Callback<T>) {
        this.mListener = listener
    }

    interface Callback<T>{
        fun onSelectedItem(position: Int,item:T)
    }

    override fun onBindViewHolder(holder: SimpleRecyclerViewHolder<*>, position: Int) {
        val model = data!![position]
        holder.binding?.setVariable(BR.viewModel, model)
        holder.binding?.root?.setOnClickListener {
            data?.get(position)?.let { it1 -> mListener?.onSelectedItem(position, it1) }
            selected = position
            notifyDataSetChanged()
        }
        holder.binding?.executePendingBindings()
    }
}