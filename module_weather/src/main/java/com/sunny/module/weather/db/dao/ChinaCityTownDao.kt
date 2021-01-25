package com.sunny.module.weather.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sunny.module.weather.db.entity.ChinaCityTownEntity

@Dao
interface ChinaCityTownDao {

    @Insert
    fun insertCity(city: ChinaCityTownEntity): Long

    @Query("select * from ChinaCityTownEntity where formattedAddress like  '%' || :cityName || '%' ")
    fun queryCity(cityName: String): List<ChinaCityTownEntity>

    @Query("select * from ChinaCityTownEntity")
    fun queryAllCity(): List<ChinaCityTownEntity>

}