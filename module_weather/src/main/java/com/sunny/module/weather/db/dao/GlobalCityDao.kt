package com.sunny.module.weather.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sunny.module.weather.db.entity.GlobalCityEntity

@Dao
interface GlobalCityDao {

    @Insert
    fun insertCity(city: GlobalCityEntity): Long

    // select * from GlobalCityEntity where cityNameEn like %cityName%
//    @Query("select * from GlobalCityEntity where cityNameZn like  '%' || :cityName || '%' or cityNameZn like  '%' || :cityName || '%' ")
//    @Query("select * from GlobalCityEntity where cityNameZn like  '%' || :cityName || '%' ")
    @Query("select * from GlobalCityEntity where cityNameEn like  '%' || :cityName || '%' ")
    fun queryCity(cityName: String): List<GlobalCityEntity>

    @Query("select * from GlobalCityEntity")
    fun queryAllCity(): List<GlobalCityEntity>
}