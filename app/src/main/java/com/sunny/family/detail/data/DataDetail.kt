package com.sunny.family.detail.data


data class DetailInfo(var itemList: List<ItemInfo>? = null)

data class ItemInfo(var img: String? = "", var name: String? = "defName", var jump: String? = "")

