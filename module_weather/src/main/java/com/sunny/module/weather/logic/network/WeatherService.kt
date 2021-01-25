package com.sunny.module.weather.logic.network

import com.sunny.module.weather.WeatherConstant
import com.sunny.module.weather.logic.model.DailyResponse
import com.sunny.module.weather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by zhangxin17 on 2021/1/25
 */
interface WeatherService {

    /**
     * https://api.caiyunapp.com/v2.5/GOcs9n3eUn3QkX3p/121.6544,25.1552/realtime.json
     * https://api.caiyunapp.com/v2.5/GOcs9n3eUn3QkX3p/116.407526,39.90403/realtime.json
     *
     */
    @GET("v2.5/${WeatherConstant.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    /**
     * https://api.caiyunapp.com/v2.5/GOcs9n3eUn3QkX3p/121.6544,25.1552/weather.json
     */
    @GET("v2.5/${WeatherConstant.TOKEN}/{lng},{lat}/weather.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}