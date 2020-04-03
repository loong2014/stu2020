package com.sunny.family.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.HandlerUtils

/**
 * Created by zhangxin17 on 2020/3/26
 */
object SunSensorManager {
    private val logTag = "Sensor-SunSensorManager"

    private val mSunSensorManager: SunSensorManager by lazy {
        SunSensorManager
    }

    fun getSensorList(callback: ISensorListCallback) {
        getSensorList(callback, false)
    }

    fun getSensorList(callback: ISensorListCallback, exceptOther: Boolean) {

        HandlerUtils.workHandler.post {

            val sensorManager: SensorManager = ContextProvider.appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

            val allList = sensorManager.getSensorList(Sensor.TYPE_ALL)

            val sensorList = mutableListOf<SensorInfo>()

            allList.forEach continuing@{
                val sensorInfo = SensorInfo()
                sensorInfo.sensor = it

                when (it.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        sensorInfo.showName = "加速度传感器"
                        sensorInfo.canShown = true
                    }

                    Sensor.TYPE_GYROSCOPE -> {
                        sensorInfo.showName = "陀螺仪传感器"
                        sensorInfo.canShown = true
                    }

                    Sensor.TYPE_LIGHT -> {
                        sensorInfo.showName = "光线传感器"
                        sensorInfo.canShown = true
                    }

                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        sensorInfo.showName = "磁场传感器"
                        sensorInfo.canShown = true
                    }

                    Sensor.TYPE_PRESSURE -> {
                        sensorInfo.showName = "气压传感器"
                        sensorInfo.canShown = true
                    }

                    Sensor.TYPE_PROXIMITY -> {
                        sensorInfo.showName = "距离传感器"
                        sensorInfo.canShown = true
                    }

                    Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                        sensorInfo.showName = "温度传感器"
                        sensorInfo.canShown = true
                    }
                    else -> {
                        sensorInfo.showName = "其它"
                    }
                }

                if (exceptOther && !sensorInfo.canShown) {
                    return@continuing
                }

                // continue === return@continuing
                // break === return@breaking

                sensorList.add(sensorInfo)
            }

            callback.onGetSensorList(sensorList)
        }
    }

}