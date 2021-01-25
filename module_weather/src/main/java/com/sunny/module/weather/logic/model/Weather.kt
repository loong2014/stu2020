package com.sunny.module.weather.logic.model

data class Location(val lng: String, val lat: String)

data class Weather(val realtime: RealtimeResult, val daily: DailyResult)


data class WeatherItemInfo(val key: String, val value: String)