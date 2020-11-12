package com.sunny.family.city.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sunny.family.R
import com.sunny.family.city.CityItemModel
import com.sunny.family.city.ItemViewTypeCity
import com.sunny.family.city.ItemViewTypeTip
import com.sunny.lib.utils.ResUtils

/**
 * Created by zhangxin17 on 2020/11/13
 */
class CityAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    constructor(data: MutableList<MultiItemEntity>) : super(data) {
        addItemType(ItemViewTypeTip, R.layout.layout_item_view_city_tip)
        addItemType(ItemViewTypeCity, R.layout.layout_item_view_city)
    }

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        val itemMode = item as CityItemModel
        val bgView: View = holder.getView(R.id.item_constraint)
        val nameView: AppCompatTextView = holder.getView(R.id.item_name)
        when (holder.itemViewType) {
            ItemViewTypeTip -> {
                nameView.text = itemMode.showName

                bgView.setBackgroundColor(ResUtils.getColor(R.color.def_item_tip_bg))
            }
            ItemViewTypeCity -> {
                nameView.text = itemMode.showName

                if (holder.bindingAdapterPosition % 2 == 0) {
                    bgView.setBackgroundColor(ResUtils.getColor(R.color.def_item_bg))
                } else {
                    bgView.setBackgroundColor(ResUtils.getColor(R.color.def_item_bg2))
                }
            }
            else -> {
            }
        }
    }
}