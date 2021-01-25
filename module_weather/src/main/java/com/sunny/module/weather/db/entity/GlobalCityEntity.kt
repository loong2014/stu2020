package com.sunny.module.weather.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zhangxin17 on 2021/1/26
 * 城市名英文,城市名中文,洲,国家代码,国家名称英文,纬度,经度
 * Tokyo,东京,亚洲,JP,Japan,35.689499,139.691711
 */
@Entity
data class GlobalCityEntity(
        val cityNameEn: String, // 城市名英文
        val cityNameZn: String, // 城市名中文
        val continents: String, // 洲
        val countryCode: String, // 国家代码
        val countryNameEn: String, // 国家名称英文
        val lng: String, // 纬度
        val lat: String // 经度
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}