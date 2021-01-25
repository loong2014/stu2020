package com.sunny.module.weather

/**
 * Created by zhangxin17 on 2021/1/25
 */
object WeatherConstant {

    // 从彩云天气开发平台申请到的令牌值
    const val TOKEN = "GOcs9n3eUn3QkX3p"


    /**
     * 天气现象代码
     */
    enum class SkyconEnum(val tip: String) {
        CLEAR_DAY("晴（白天）"),
        CLEAR_NIGHT("晴（夜间）"),
        PARTLY_CLOUDY_DAY("多云（白天）"),
        PARTLY_CLOUDY_NIGHT("多云（夜间）"),
        CLOUDY("阴"),
        LIGHT_HAZE("轻度雾霾"),
        MODERATE_HAZE("中度雾霾"),
        HEAVY_HAZE("重度雾霾"),
        LIGHT_RAIN("小雨"),
        MODERATE_RAIN("中雨"),
        HEAVY_RAIN("大雨"),
        STORM_RAIN("暴雨"),
        FOG("雾"),
        LIGHT_SNOW("小雪"),
        MODERATE_SNOW("中雪"),
        HEAVY_SNOW("大雪"),
        STORM_SNOW("暴雪"),
        DUST("浮尘"),
        SAND("沙尘"),
        WIND("大风")
    }

    fun getSkyconTip(key: String): String {
        return SkyconEnum.valueOf(key).tip
    }


}