package com.sunny.family.city

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by zhangxin17 on 2020/11/13
 */
const val ItemViewTypeTip = 1
const val ItemViewTypeCity = 2

data class CityItemModel(var viewType: Int, var dataType: Int, var showName: String, var cityCount: Int = 0) : MultiItemEntity {
    override val itemType: Int
        get() = viewType

    fun isCity(): Boolean {
        return ItemViewTypeCity == itemType
    }

    fun isTip(): Boolean {
        return ItemViewTypeTip == itemType
    }
}

fun buildCityData(): MutableList<MultiItemEntity> {
    val list = mutableListOf<MultiItemEntity>()

    //
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 1, showName = "4直辖市", cityCount = 4))
    var nameList = arrayOf("北京市", "天津市", "上海市", "重庆市")
    for (name in nameList) {
        list.add(CityItemModel(viewType = ItemViewTypeCity, dataType = 1, showName = name))
    }

    //
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 2, showName = "23省", cityCount = 23))
    nameList = arrayOf("黑龙江省", "吉林省", "辽宁省",
            "河北省", "山西省", "陕西省", "广东省", "湖南省",
            "湖北省", "江苏省", "安徽省", "浙江省", "福建省",
            "江西省", "云南省", "贵州省", "四川省", "青海省",
            "山东省", "河南省", "甘肃省", "海南省", "台湾省")
    for (name in nameList) {
        list.add(CityItemModel(viewType = ItemViewTypeCity, dataType = 2, showName = name))
    }

    //
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 3, showName = "5自治区", cityCount = 5))
    nameList = arrayOf("内蒙古自治区", "广西壮族自治区", "西藏自治区", "宁夏回族自治区", "新疆维吾尔自治区")
    for (name in nameList) {
        list.add(CityItemModel(viewType = ItemViewTypeCity, dataType = 3, showName = name))
    }

    //
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 4, showName = "2特别行政区", cityCount = 2))
    nameList = arrayOf("香港特别行政区", "澳门特别行政区")
    for (name in nameList) {
        list.add(CityItemModel(viewType = ItemViewTypeCity, dataType = 4, showName = name))
    }

    return list
}


fun buildCityTipData(): MutableList<MultiItemEntity> {
    val list = mutableListOf<MultiItemEntity>()
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 1, showName = "4直辖市"))
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 2, showName = "23省"))
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 3, showName = "5自治区"))
    list.add(CityItemModel(viewType = ItemViewTypeTip, dataType = 4, showName = "2特别行政区"))
    return list
}
