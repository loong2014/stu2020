package com.sunny.family.device

import android.os.Build
import com.sunny.lib.utils.SunDeviceUtils
import com.sunny.lib.utils.SunLog
import com.sunny.lib.utils.SunNetworkModel
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * Created by zhangxin17 on 2020/8/18
 */
const val TAG = "SunnyDeviceInfo"

class DeviceInfoModel {

    val sdkCode: Int by lazy {
        Build.VERSION.SDK_INT
    }

    val cpuInfo: String by lazy {
        doGetCpuInfo()
    }

    val macInfo: String by lazy {
        SunDeviceUtils.mac
    }

    val snInfo: String by lazy {
        SunDeviceUtils.sn
    }

    val networkModel by lazy {
        SunNetworkModel()
    }

    private fun doGetCpuInfo(): String {

        val sb = StringBuilder()

        val cpuCount = doGetCpuCount()
        sb.append("${cpuCount}核")

        val arch = getCpuArch()
        sb.append(" $arch")

        val cpuInfo = sb.toString()
        SunLog.i(TAG, "doGetCpuInfo :$cpuInfo")
        return cpuInfo
    }

    /**
     * 获取cpu架构，通过读取属性
     */
    private fun getCpuArch(): String {
        var arch = "armeabi"
        try {
            val cpuArch = BufferedReader(InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").inputStream)).readLine()

            arch = when {
                cpuArch.contains("x86") -> "x86"

                cpuArch.contains("armeabi-v7a") ||
                        cpuArch.contains("arm64-v8a") -> "armeabi-v7a"

                else -> "aremabi"
            }
        } catch (e: Exception) {
            SunLog.e(TAG, "getCpuArch error :$e")
        }
        return arch
    }

    /**
     * 获取cpu个数
     */
    private fun doGetCpuCount(): Int {
        var count = 1
        try {
            val dir = File("/sys/devices/system/cpu/")
            val files = dir.listFiles { it -> Pattern.matches("cpu[0-9]", it.name) }
            count = files.size
        } catch (e: Exception) {
            SunLog.e(TAG, "doGetCpuInfo error :$e")
        }
        return count
    }


}