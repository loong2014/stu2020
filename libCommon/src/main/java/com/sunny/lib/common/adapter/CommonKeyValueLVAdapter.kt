package com.sunny.lib.common.adapter

import android.content.Context
import android.widget.TextView
import com.sunny.lib.common.R

open class CommonKeyValueLVAdapter(
    context: Context,
    dataList: List<CommonKeyValueInfo>? = null
) : CommonLVAdapter<CommonKeyValueInfo>(context, R.layout.layout_common_item_key_value, dataList) {

    override fun convert(viewHolder: CommonLVViewHolder, data: CommonKeyValueInfo) {
        viewHolder.getView<TextView>(R.id.item_key).text = data.key
        viewHolder.getView<TextView>(R.id.item_value).text = data.value
    }
}