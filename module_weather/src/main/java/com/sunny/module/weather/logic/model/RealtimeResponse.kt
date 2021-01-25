package com.sunny.module.weather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: RealtimeResult)

data class RealtimeResult(val realtime: Realtime)

data class Realtime(val skycon: String,
                    val visibility: Float,
                    val humidity: Float,
                    val temperature: Float,
                    val wind: Wind? = null,
                    val pressure: Float,
                    @SerializedName("air_quality") val airQuality: AirQuality? = null)


data class AirQuality(val pm25: Float,
                      val o3: Float,
                      val co: Float,
                      val aqi: AQI? = null)

data class AQI(val chn: Float)

data class Wind(val direction: Float, val speed: Float)