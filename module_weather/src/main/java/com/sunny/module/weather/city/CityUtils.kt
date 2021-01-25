package com.sunny.module.weather.city

import com.sunny.lib.base.log.SunLog
import com.sunny.lib.common.utils.SunSharedPreferencesUtils
import com.sunny.lib.utils.ContextProvider
import com.sunny.module.weather.db.CityDatabase
import com.sunny.module.weather.db.entity.ChinaCityTownEntity
import com.sunny.module.weather.db.entity.GlobalCityEntity
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

object CityUtils {

    private const val TAG = "CityUtils"
    private const val GlobalSPKey = "db_load_global_city"
    private const val ChinaSPKey = "db_load_china_city"

    val cityDatabase: CityDatabase by lazy {
        CityDatabase.getDatabase(ContextProvider.appContext)
    }

    fun tryLoadCsvDataToDb() {

        tryLoadChinaCityTownInfoToDb()

        tryLoadGlobalCityInfoToDb()
    }

    fun hasLoadCityInfo(): Boolean {
        val hasLoadGlobal = SunSharedPreferencesUtils.getBoolean(GlobalSPKey, false)
        val hasLoadChina = SunSharedPreferencesUtils.getBoolean(ChinaSPKey, false)
        return hasLoadGlobal && hasLoadChina
    }

    private fun tryLoadGlobalCityInfoToDb() {
        val hasLoaded = SunSharedPreferencesUtils.getBoolean(GlobalSPKey, false)
        SunLog.i(TAG, "tryLoadGlobalCityInfoToDb  hasLoaded :$hasLoaded")
        if (hasLoaded) {
            return
        }

        thread {
            try {
                val assetManager = ContextProvider.appContext.assets
                val isr = InputStreamReader(assetManager.open("global_cities.csv"))
                val reader = BufferedReader(isr)

                reader.readLine() // csv文件的第一行数据需要舍弃

                var totalCount = 0
                var insertCount = 0
                reader.use {
                    reader.forEachLine { line: String ->
                        totalCount++

                        buildGlobalCityEntity(line)?.let {

                            val id = cityDatabase.globalCityTownDao().insertCity(it)
                            insertCount++
                            SunLog.i(TAG, "insertCity  $id : ${it.cityNameZn}")
                        }
                    }
                }

                //
                SunSharedPreferencesUtils.putBoolean(GlobalSPKey, true)

                SunLog.i(TAG, "tryLoadGlobalCityInfoToDb  success :$totalCount($insertCount)")

            } catch (e: Exception) {
                SunLog.i(TAG, "tryLoadGlobalCityInfoToDb error :$e")
            }
        }
    }

    private fun tryLoadChinaCityTownInfoToDb() {
        val needLoad = SunSharedPreferencesUtils.getBoolean(ChinaSPKey, false)
        SunLog.i(TAG, "tryLoadChinaCityTownInfoToDb  needLoad :$needLoad")

        if (needLoad) {
            thread {
                try {
                    val assetManager = ContextProvider.appContext.assets
                    val isr = InputStreamReader(assetManager.open("china_cities.csv"))
                    val reader = BufferedReader(isr)

                    reader.readLine() // csv文件的第一行数据需要舍弃

                    var totalCount = 0
                    var insertCount = 0
                    reader.use {
                        reader.forEachLine { line: String ->
                            totalCount++

                            buildChinaCityTownEntity(line)?.let {

                                val id = cityDatabase.chinaCityTownDao().insertCity(it)
                                insertCount++
                                SunLog.i(TAG, "insertCity  $id : ${it.formattedAddress}")
                            }
                        }
                    }

                    //
                    SunSharedPreferencesUtils.putBoolean(ChinaSPKey, true)

                    SunLog.i(TAG, "tryLoadChinaCityTownInfoToDb  success :$totalCount($insertCount)")

                } catch (e: Exception) {
                    SunLog.i(TAG, "tryLoadChinaCityTownInfoToDb error :$e")
                }
            }
        }
    }

    /**
     * 110109,116.102009,39.940646,北京市,北京市,门头沟区,北京市门头沟区
     */
    private fun buildChinaCityTownEntity(str: String): ChinaCityTownEntity? {
        val datas = str.split(",")
        if (datas.size != 7) {
            return null
        }
        return ChinaCityTownEntity(datas[0], datas[1], datas[2], datas[3], datas[4], datas[5], datas[6])
    }

    /**
     * Tokyo,东京,亚洲,JP,Japan,35.689499,139.691711
     */
    private fun buildGlobalCityEntity(str: String): GlobalCityEntity? {
        val datas = str.split(",")
        if (datas.size != 7) {
            return null
        }
        return GlobalCityEntity(datas[0], datas[1], datas[2], datas[3], datas[4], datas[5], datas[6])
    }

}