package com.sunny.module.weather.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zhangxin17 on 2021/1/26
 * adcode,lng,lat,province,city,district,formatted_address
 * 110000,116.407526,39.90403,北京市,北京市,,北京市
 * 110100,116.407526,39.90403,北京市,北京市,,北京市市辖区
 * 110101,116.416357,39.928353,北京市,北京市,东城区,北京市东城区
 */
@Entity
data class ChinaCityTownEntity(
        var adcode: String, // 地区编码
        var lng: String, // 纬度
        var lat: String, // 经度
        var province: String, // 所属省
        var city: String, // 所属市
        var district: String, // 所属县
        var formattedAddress: String // 完整地址
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
