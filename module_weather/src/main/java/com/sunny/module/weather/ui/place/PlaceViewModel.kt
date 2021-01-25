package com.sunny.module.weather.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunny.lib.base.log.SunLog
import com.sunny.module.weather.city.CityUtils
import com.sunny.module.weather.city.PlaceInfo
import kotlin.concurrent.thread

class PlaceViewModel() : ViewModel() {
    companion object {
        const val TAG = "CityViewModel"
    }

    val queryResultCount: LiveData<Int>
        get() = _resultCount

    private val _resultCount = MutableLiveData<Int>()

    private var _queryResultList = ArrayList<PlaceInfo>()

    private fun updateResultList(list: ArrayList<PlaceInfo>) {
        _queryResultList = list
        _resultCount.postValue(_queryResultList.size)
    }

    fun clearResult() {
        _queryResultList.clear()
        _resultCount.postValue(0)
    }

    fun getQueryResultList(): ArrayList<PlaceInfo> {
        return _queryResultList
    }

    fun queryCityInfo(nameKey: String) {
        queryGlobalCityInfo(nameKey)
    }

    private fun queryChinaCityInfo(nameKey: String) {
        thread {
            val resultCities = CityUtils.cityDatabase.chinaCityTownDao().queryCity(nameKey)
            SunLog.i(TAG, "queryChinaCityInfo  count :${resultCities.size}")

            val list = ArrayList<PlaceInfo>()
            for (city in resultCities) {
                list.add(PlaceInfo(city.formattedAddress, city.lng, city.lat))
            }
            updateResultList(list)
        }
    }

    private fun queryGlobalCityInfo(nameKey: String) {
        thread {
            val resultCities = CityUtils.cityDatabase.globalCityTownDao().queryCity(nameKey)
            SunLog.i(TAG, "queryChinaCityInfo  count :${resultCities.size}")

            val list = ArrayList<PlaceInfo>()
            for (city in resultCities) {
                list.add(PlaceInfo(city.cityNameZn, city.lng, city.lat, city.cityNameEn))
            }
            updateResultList(list)
        }
    }
}