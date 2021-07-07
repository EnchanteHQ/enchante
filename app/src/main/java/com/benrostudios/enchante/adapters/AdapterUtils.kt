package com.benrostudios.enchante.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T : Any> RecyclerView.withSimpleAdapter(
    dataList: List<T>, @LayoutRes layoutID: Int,
    onBindView: BaseViewHolder<T>.(data: T) -> Unit
): SimpleRecyclerAdapter<T> {
    val recyclerAdapter = SimpleRecyclerAdapter(dataList, layoutID, onBindView)
    adapter = recyclerAdapter
    return recyclerAdapter
}