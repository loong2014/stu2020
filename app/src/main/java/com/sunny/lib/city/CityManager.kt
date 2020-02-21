package com.sunny.lib.city

import com.google.gson.Gson
import com.sunny.lib.utils.SunAssetsUtils

/**
 * 中国有34个省级行政区域，其中包括23个省、5个自治区、4个直辖市,以及2个特别行政区
 */
object CityManager {

    private const val assetsChinaCityListFileName = "china_city_list.json"

    val chinaCityInfo: ChinaCityInfo? by lazy {
        getChinaCityList()
    }

    val chinaCityList: List<CityInfo> by lazy {
        sortCityList(chinaCityInfo?.city_list)
    }

    private fun getChinaCityList(): ChinaCityInfo? {
        val cityListStr = SunAssetsUtils.getLocalAssetsFileStr(assetsChinaCityListFileName)
        return Gson().fromJson(cityListStr, ChinaCityInfo::class.java)
    }

    private fun sortCityList(list: List<CityInfo>?): List<CityInfo> {

        if (list == null) {
            return mutableListOf()
        }

        val normalList = mutableListOf<CityInfo>()

        val directList = mutableListOf<CityInfo>()

        val autonomyList = mutableListOf<CityInfo>()

        val specialList = mutableListOf<CityInfo>()

        list.forEach {
            when (it.type) {
                CityType.Direct -> directList.add(it)

                CityType.Normal -> normalList.add(it)

                CityType.Autonomy -> autonomyList.add(it)

                CityType.Special -> specialList.add(it)

                else -> normalList.add(it)
            }
        }

//        directList.add(0, CityInfo(type = CityType.Tip, name = "${directList.size}个直辖市"))
//
//        normalList.add(0, CityInfo(type = CityType.Tip, name = "${normalList.size}个省"))
//
//        autonomyList.add(0, CityInfo(type = CityType.Tip, name = "${autonomyList.size}个自治区"))
//
//        specialList.add(0, CityInfo(type = CityType.Tip, name = "${specialList.size}个特别行政区"))

        val outList = mutableListOf<CityInfo>()

        outList.add(CityInfo(type = CityType.Tip, name = "${directList.size}个直辖市", children = directList))
        outList.add(CityInfo(type = CityType.Tip, name = "${normalList.size}个省", children = normalList))
        outList.add(CityInfo(type = CityType.Tip, name = "${autonomyList.size}个自治区", children = autonomyList))
        outList.add(CityInfo(type = CityType.Tip, name = "${specialList.size}个特别行政区", children = specialList))
        return outList
    }
}