package com.sunny.family.detail

import com.sunny.lib.city.CityManager
import com.sunny.lib.city.model.CityInfo
import com.sunny.lib.utils.HandlerUtils

class SunDetailHelper(callBack: DetailCallback) {

    private val mCallback: DetailCallback = callBack

    private val logTag = "Detail-SunDetailHelper"

    fun doGetCityList() {
        HandlerUtils.workHandler.post {
            val cityList: List<CityInfo>? = CityManager.chinaCityList
            mCallback.onGetCityList(cityList)
        }
    }

    interface DetailCallback {
        fun onGetCityList(list: List<CityInfo>?)
    }
}