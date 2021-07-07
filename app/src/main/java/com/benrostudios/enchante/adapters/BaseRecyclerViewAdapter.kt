package com.benrostudios.enchante.adapters

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Data : Any>(protected var dataList: List<Data>): RecyclerView.Adapter<BaseViewHolder<Data>>() {
    abstract val layoutItemId: Int

    override fun getItemCount(): Int = dataList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Data> =
        BaseViewHolder(
            parent,
            layoutItemId
        )
}
class BaseViewHolder<in T>(parent: ViewGroup, @LayoutRes layoutId: Int) :
    RecyclerView.ViewHolder(parent.inflater(layoutId))


