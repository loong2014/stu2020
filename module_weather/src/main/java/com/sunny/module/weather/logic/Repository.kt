package com.sunny.module.weather.logic

import androidx.lifecycle.liveData
import com.sunny.module.weather.logic.model.Weather
import com.sunny.module.weather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object Repository {

    fun refreshWeather(lng: String, lat: String) = liveData<Result<Weather>> {
        val result = try {
            coroutineScope {
                val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
                val deferredDaily = async { SunnyWeatherNetwork.getDailyService(lng, lat) }

                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()

                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(realtimeResponse.result, dailyResponse.result)
                    Result.success(weather)

                } else {
                    Result.failure(RuntimeException("realtimeResponse status is ${realtimeResponse.status} , " +
                            "dailyResponse status is ${dailyResponse.status}"))
                }
            }
        } catch (e: Exception) {
            Result.failure<Weather>(e)
        }
        emit(result)
    }


//    fun refreshWeather(lng: String, lat: String) = liveData<Result<RealtimeResult>> {
//        val result = try {
//            val weatherResponse = SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
//            if (weatherResponse.status == "ok") {
//                val realtime = weatherResponse.result
//                Result.success(realtime)
//
//            } else {
//
//                Result.failure(RuntimeException("response status is ${weatherResponse.status}"))
//            }
//        } catch (e: Exception) {
//            Result.failure<RealtimeResult>(e)
//        }
//        emit(result)
//    }
}