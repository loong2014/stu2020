package com.sunny.family.home.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sunny.family.R
import com.sunny.family.home.HomeItemModel

/**
 * Created by zhangxin17 on 2020/11/13
 */
class HomeAdapter : BaseQuickAdapter<HomeItemModel, BaseViewHolder> {

    constructor(data: MutableList<HomeItemModel>) : super(R.layout.layout_item_view_home, data) {
    }

    override fun convert(holder: BaseViewHolder, item: HomeItemModel) {
        val nameView: AppCompatTextView = holder.getView(R.id.item_name)
        nameView.text = item.showName
    }
}