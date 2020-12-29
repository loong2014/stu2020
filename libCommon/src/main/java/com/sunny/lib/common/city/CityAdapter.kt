package com.sunny.lib.common.city

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sunny.lib.common.R
import com.sunny.lib.common.utils.ResUtils

/**
 * Created by zhangxin17 on 2020/11/13
 */
class CityAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private lateinit var mContext: Context

    constructor(context: Context, data: MutableList<MultiItemEntity>) : this(data) {
        mContext = context
    }

    constructor(data: MutableList<MultiItemEntity>) : super(data) {
        addItemType(ItemViewTypeTip, R.layout.layout_item_view_city_tip)
        addItemType(ItemViewTypeCity, R.layout.layout_item_view_city)
        addItemType(ItemViewTypeSwipe, R.layout.layout_item_view_swipe)
    }

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        val itemMode = item as CityItemModel
        when (holder.itemViewType) {
            ItemViewTypeTip -> {
                val nameView: AppCompatTextView = holder.getView(R.id.item_name)
                nameView.text = itemMode.showName

                val bgView: View = holder.getView(R.id.item_constraint)
                bgView.setBackgroundColor(ResUtils.getColor(R.color.def_item_tip_bg))
            }
            ItemViewTypeCity -> {

                val nameView: AppCompatTextView = holder.getView(R.id.item_name)
                nameView.text = itemMode.showName

                val bgView: View = holder.getView(R.id.item_constraint)
                if (holder.adapterPosition % 2 == 0) {
                    bgView.setBackgroundColor(ResUtils.getColor(R.color.def_item_bg))
                } else {
                    bgView.setBackgroundColor(ResUtils.getColor(R.color.def_item_bg2))
                }
            }
            else -> {
            }
        }
    }

    private fun setSwipeData(holder: BaseViewHolder) {
        val mSwipeRecyclerView: RecyclerView = holder.getView(R.id.swipe_recyclerView)
        mSwipeRecyclerView.setLayoutManager(GridLayoutManager(mContext, 1))
        mSwipeRecyclerView.setHasFixedSize(true)
        val mSwipeAdapter = CityAdapter(buildCityTipData())
        mSwipeRecyclerView.setAdapter(mSwipeAdapter)
    }
}