package com.sunny.family.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class CommonAdapter<T>(private val context: Context,
                                private val dataList: List<T>?,
                                private val viewLayoutId: Int) : BaseAdapter() {

    override fun getCount(): Int {
        if (dataList.isNullOrEmpty()) {
            return 0
        }
        return dataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): T? {
        if (!dataList.isNullOrEmpty() && position < dataList.size) {
            return dataList[position]
        }
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // 实例化一个ViewHolder
        val holder = ViewHolder.get(context = context, convertView = convertView, parent = parent, layoutId = viewLayoutId)

        getItem(position)?.let {
            // 数据绑定
            convert(holder, it)
        }
        return holder.getConvertView()
    }

    abstract fun convert(viewHolder: ViewHolder, data: T)

}