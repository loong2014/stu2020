package com.sunny.module.view.xxx.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sunny.module.view.R

/**
 * Created by zhangxin17 on 2020/11/13
 */
class StringAdapter : BaseQuickAdapter<StringModel, BaseViewHolder> {

    constructor(data: MutableList<StringModel>) : super(R.layout.view_item_string, data) {
    }

    override fun convert(holder: BaseViewHolder, item: StringModel) {
        val nameView: AppCompatTextView = holder.getView(R.id.item_name)
        nameView.text = item.name
    }
}