package com.sunny.module.home.developer.device

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunny.lib.base.log.SunLog
import com.sunny.lib.base.utils.SunNetworkUtils
import com.sunny.lib.common.utils.SunDeviceUtils
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import java.util.regex.Pattern
import kotlin.random.Random

class DeviceViewModel : ViewModel() {

    // 设备信息
    var deviceInfo = MutableLiveData<Int>()

    var sdkVersion: String = "xxx"

    var mac: String = "xxx"

    var deviceName: String = "xxx"

    var snCode: String = "xxx"

    var systemUiVersion: String = "xxx"
    var systemSoftVersion: String = "xxx"

    var internetAddress: String = "xxx"

    fun doGetDeviceInfo() {
        deviceName = Build.DEVICE

        sdkVersion = Build.VERSION.SDK_INT.toString()

        mac = SunDeviceUtils.mac

        snCode = Build.MODEL

        systemUiVersion = SunDeviceUtils.getSystemProperty("ro.letv.release.version")
        systemSoftVersion = SunDeviceUtils.getSystemProperty("ro.build.id")

        internetAddress = if (SunNetworkUtils.hasActiveNetwork()) {
            SunNetworkUtils.getIpAddress()
        } else {
            "0.0.0.0"
        }

        //
        deviceInfo.value = Random.nextInt()
    }


    /**
     * 获取链接wifi时的ip地址
     */
    private fun getWifiIp(): String {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en
                            .hasMoreElements()) {
                val network: NetworkInterface = en.nextElement()
                if (network.getName().toLowerCase().equals("eth0")
                        || network.getName().toLowerCase().equals("wlan0")) { // 仅过滤无线和有线的ip
                    val enumIp: Enumeration<InetAddress> = network.getInetAddresses()
                    while (enumIp.hasMoreElements()) {
                        val inetAddress: InetAddress = enumIp.nextElement()
                        if (!inetAddress.isLoopbackAddress()) {
                            val ip: String = inetAddress.getHostAddress()
                            if (!ip.contains("::")) { // 过滤掉ipv6的地址
                                return ip
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * https://blog.csdn.net/android_cai_niao/article/details/106329664
     * 获取本机ip地址列表，Wifi、电话卡和网线的ip都能获取到（手机也能插网线的）
     */
    private fun getLocalIpList() = NetworkInterface.getNetworkInterfaces().asSequence()
            // 过滤：已启动并且正在运行的接口，且要有ip地址，且不能是环回接口（即把数据发送给本机的网络接口，ipv4为127.0.0.1，ipv6为::1）
            .filter { ni: NetworkInterface -> ni.isUp && ni.inetAddresses.toList().isNotEmpty() && !ni.isLoopback }

            // 把每个网络接口中的ip地址列表合成一个大列表
            .flatMap { ni: NetworkInterface -> ni.inetAddresses.asSequence() }

            // 过滤：如果是ipv6，不能是连接本地地址
            .filter { ip: InetAddress -> !(ip is Inet6Address && ip.isLinkLocalAddress) }

            .toList()

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