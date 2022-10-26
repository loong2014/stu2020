package com.sunny.lib.common.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class CommonLVAdapter<T>(
    private val context: Context,
    private val viewLayoutId: Int,
    private var dataList: List<T>? = null
) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): T? {
        return dataList?.takeIf { position in it.indices }?.get(position)
    }

    fun updateData(list: List<T>?) {
        dataList = list
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // 实例化一个ViewHolder
        val holder = CommonLVViewHolder.get(
            context = context,
            convertView = convertView,
            parent = parent,
            layoutId = viewLayoutId
        )

        getItem(position)?.let {
            // 数据绑定
            convert(holder, it)
        }
        return holder.getConvertView()
    }

    abstract fun convert(viewHolder: CommonLVViewHolder, data: T)

}