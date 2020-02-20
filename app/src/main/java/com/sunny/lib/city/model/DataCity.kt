package com.sunny.lib.city.model

object CityType {
    const val Normal = 0 // 普通省份
    const val Direct = 1 // 直辖市
    const val Autonomy = 2 // 自治区
    const val Special = 3 // 特别行政区
    const val Tip = 4  // 提示
}

data class ChinaCityInfo(val city_list: List<CityInfo>? = null)

data class CityInfo(val code: String? = "code", var type: Int = 0, var name: String? = "name", val children: List<CityInfo>? = null)