package com.sunny.lib.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import java.net.NetworkInterface

/**
 * Created by zhangxin17 on 2020/8/18
 */
object SunDeviceUtils {

    val TAG = "SunDeviceUtils"

    /**
     * 获取设备key
     */
    @JvmStatic
    val deviceKey: String by lazy {
        ""
    }

    /**
     * 获取设备的mac地址
     */
    @JvmStatic
    val mac: String by lazy {
        when {
            // 6.0之后
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                getMacFromNetwork()
            }
            // 6.0之前
            else -> {
                getMacFromWifiManager()
            }
        }
    }

    /**
     * 6.0之前，通过wifiManager
     * 6.0之后获取到的恒为：02:00:00:00:00:00
     *
     */
    private fun getMacFromWifiManager(): String {
        val manager = ContextProvider.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = manager.connectionInfo
        wifiInfo?.let {
            return it.macAddress
        }
        return ""
    }

    /**
     * 6.0 ～ 7.0，通过网络接口
     * 获取网卡的mac地址
     */
    private fun getMacFromNetwork(): String {
        try {
            //获取本机器所有的网络接口
            val enumeration = NetworkInterface.getNetworkInterfaces()
            while (enumeration.hasMoreElements()) {
                val networkInterface = enumeration.nextElement()

                //获取硬件地址，一般是MAC
                val macArray = networkInterface.hardwareAddress
                if (macArray == null || macArray.isEmpty()) {
                    continue
                }

                val sb = StringBuilder()
                for (b in macArray) {
                    //格式化为：两位十六进制加冒号的格式，若是不足两位，补0
                    sb.append(String.format("%02x:", b))
                }
                //删除后面多余的冒号
                sb.deleteCharAt(sb.length - 1)
                val mac = sb.toString()
                SunLog.i(TAG, "getMacFromNetwork  ${networkInterface.name} : $mac")

                if ("eth0" == networkInterface.name) {
                    return mac
                }
            }
        } catch (e: Exception) {
            SunLog.e(TAG, "get mac error :$e")
        }
        return ""
    }

    /**
     * 设备序列号
     */
    @JvmStatic
    val sn: String by lazy {
        when {
            // 9.0及以上
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                Build.getSerial()
            }
            // 8.0以上
            Build.VERSION.SDK_INT > Build.VERSION_CODES.N -> {
                Build.USER
            }
            // 8.0及以下
            else -> {
                getSystemProperty("ro.serialno", "")
            }
        }
    }

    /**
     * 获取系统属性值
     */
    @JvmStatic
    fun getSystemProperty(key: String): String {
        return getSystemProperty(key, "")
    }

    @JvmStatic
    fun getSystemProperty(key: String, defValue: String): String {
        var value = defValue

        try {
            val systemPropertyClass = Class.forName("android.os.SystemProperties")
            val getMethod = systemPropertyClass.getDeclaredMethod("get", String::class.java, String::class.java)

            value = getMethod.invoke(null, key, defValue).toString()
        } catch (e: Exception) {
            SunLog.e(TAG, "getSystemProperty($key) error :$e")
        }

        SunLog.i(TAG, "getSystemProperty($key) value :$value")

        return value
    }


}