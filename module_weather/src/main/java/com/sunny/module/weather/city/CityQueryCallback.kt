package com.sunny.module.weather.city


data class PlaceInfo(val name: String, val lng: String, val lat: String, val nameEn: String = "")

interface CityQueryCallback {
    fun onCallback(places: List<PlaceInfo>)
}