package com.sunny.module.weather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: RealtimeResult)

data class RealtimeResult(val realtime: Realtime)

data class Realtime(val skycon: String, val temperature: Float,
                    @SerializedName("air_quality") val airQuality: AirQuality)

data class AirQuality(val aqi: AQI)

data class AQI(val chn: Float)