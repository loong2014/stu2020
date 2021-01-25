package com.sunny.module.weather.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sunny.module.weather.db.dao.ChinaCityTownDao
import com.sunny.module.weather.db.dao.GlobalCityDao
import com.sunny.module.weather.db.entity.ChinaCityTownEntity
import com.sunny.module.weather.db.entity.GlobalCityEntity

@Database(version = 1, entities = [ChinaCityTownEntity::class, GlobalCityEntity::class])
abstract class CityDatabase : RoomDatabase() {

    abstract fun chinaCityTownDao(): ChinaCityTownDao

    abstract fun globalCityTownDao(): GlobalCityDao

    companion object {
        private var instance: CityDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): CityDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                    CityDatabase::class.java, "city_database")
                    .build().apply {
                        instance = this
                    }
        }
    }
}