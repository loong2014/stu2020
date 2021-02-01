package com.sunny.module.weather.ui.weather

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sunny.module.weather.R
import com.sunny.module.weather.logic.model.WeatherItemInfo

class WeatherInfoAdapter(data: MutableList<WeatherItemInfo>) :
        BaseQuickAdapter<WeatherItemInfo, BaseViewHolder>(R.layout.weather_item, data) {

    override fun convert(helper: BaseViewHolder, item: WeatherItemInfo) {

        //
        val nameView: AppCompatTextView = helper.getView(R.id.tipName)
        nameView.text = item.key

        //
        val valueView: AppCompatTextView = helper.getView(R.id.tvValue)
        valueView.text = item.value
    }
}