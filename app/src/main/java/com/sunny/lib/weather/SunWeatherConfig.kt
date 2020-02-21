package com.sunny.lib.weather

import heweather.com.weathernetsdk.view.HeWeatherConfig

/**
 * https://cj.weather.com.cn/plugin/sdk
 * key:05Htvxl7FH
 *
 * 获取天气信息：http://api.p.weatherdt.com/plugin/data?key=05Htvxl7FH&location=郑州
 *
 * webView：https://apip.weatherdt.com/sdk/weather.html?location=郑州
 */
object SunWeatherConfig {
    const val UserKey: String = "05Htvxl7FH"

    fun initWeatherCity(location: String) {
        HeWeatherConfig.init(UserKey, location)
    }
}