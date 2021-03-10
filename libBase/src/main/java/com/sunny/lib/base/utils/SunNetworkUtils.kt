package com.sunny.lib.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import com.sunny.lib.utils.ContextProvider
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*


object SunNetworkUtils {


    /**
     * https://blog.csdn.net/android_cai_niao/article/details/106329664
     * 获取本机ip地址列表，Wifi、电话卡和网线的ip都能获取到（手机也能插网线的）
     */
    @JvmStatic
    fun getLocalIpList() = NetworkInterface.getNetworkInterfaces().asSequence()
            // 过滤：已启动并且正在运行的接口，且要有ip地址，且不能是环回接口（即把数据发送给本机的网络接口，ipv4为127.0.0.1，ipv6为::1）
            .filter { ni: NetworkInterface -> ni.isUp && ni.inetAddresses.toList().isNotEmpty() && !ni.isLoopback }

            // 把每个网络接口中的ip地址列表合成一个大列表
            .flatMap { ni: NetworkInterface -> ni.inetAddresses.asSequence() }

            // 过滤：如果是ipv6，不能是连接本地地址
            .filter { ip: InetAddress -> !(ip is Inet6Address && ip.isLinkLocalAddress) }

            .toList()


    /**
     * 网络是否可用
     */
    @JvmStatic
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun hasActiveNetwork(): Boolean {
        val manager: ConnectivityManager = ContextProvider.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 当关闭“数据连接”开关时，而且没有Wifi，则activeNetwork或activeNetworkInfo为null
        // 当sim卡过期不管有无信号，activeNetwork或activeNetworkInfo为null
        // 只要有sim卡或者连接上wifi，不论是否能连接互联网，都会返回true。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0或以上
            val activeNetwork = manager.activeNetwork
            if (activeNetwork != null) {
                val networkCapabilities = manager.getNetworkCapabilities(activeNetwork)
                return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        ?: false
            }
            return false
        } else {
            // Android 6.0以下
            // 在启动网络流量之前，应始终检查isConnected（）。如果没有默认网络，则可能返回null。
            // 插着手机卡，连接上Wifi时，Wifi会变成活动网络。
            val ani = manager.activeNetworkInfo
            return ani?.isConnected ?: false
        }
    }

    @JvmStatic
    fun getNetworkInfo() {
        val manager: ConnectivityManager = ContextProvider.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0或以上
            // connectivityManager.allNetworks 获取所有网络
            val network = manager.activeNetwork // 只获取活动网络的网络
            val nc: NetworkCapabilities = manager.getNetworkCapabilities(network)// 获取网络信息对象
            // 判断是否有可用的网络（不能确保网络是否能连接互联网）
            nc?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            // 判断是否有可以连接互联网的网络（有的手机使用此方法并不准确）
            nc?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
            // 判断是否是wifi网络
            nc?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
            // 判断是否是蜂窝网络
            nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
//             判断是否是WPN网络
//            nc?.hasTransport(NetworkCapabilities.TRANSPORT_APN) ?: false

        } else {
            // Android 6.0以下
            // connectivityManager.allNetworkInfo 获取所有网络的网络信息
            val ani = manager.activeNetworkInfo // 只获取活动网络的网络信息
            ani.extraInfo    // APN，如：3gwap或"Dazhou2105"（连Wifi时）。报告有关网络状态的额外信息（如果较低的网络层提供了这些信息）。
            ani.reason        //如：connected。报告尝试建立连接失败的原因（如果有）。
            ani.state        // 如：CONNECTED。报告网络的当前粗粒度状态。
            ani.subtypeName    // 如：LTE。返回描述网络子类型的易于理解的名称。
            ani.type        // 网络类型，int类型，对应ConnectivityManager中定义的常量
            ani.typeName    // 如：MOBILE。返回一个易于理解的名称，描述网络的类型，例如“ WIFI”或“ MOBILE”。
            ani.isAvailable
            ani.isConnected // 连Wifi时，就算wifi路由没插网线也会返回true。也就是说connected为true不代码可以访问互联网，要用ping的方式或者连接服务器的方式
            ani.isConnectedOrConnecting
        }
    }

    /**
     * 获取网络类型
     */
    @JvmStatic
    fun getNetworkType(): Int {
        val manager: ConnectivityManager = ContextProvider.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // getNetworkPreference() 检索当前的首选网络类型。
        // setNetworkPreference(preference: Int) 指定首选的网络类型
        // 当系统的默认数据网络处于活动状态时，开始收听报告，这是执行网络流量的好时机。
        // addDefaultNetworkActiveListener(l: ConnectivityManager.OnNetworkActiveListener!)


        val networkInfo = manager.activeNetworkInfo
        networkInfo?.let {
            return networkInfo.type
        }
        return -1
    }

    /**
     * 是否是wifi网络
     */
    @JvmStatic
    fun isWifi(): Boolean {
        return ConnectivityManager.TYPE_WIFI == getNetworkType()
    }

    /**
     * 获取ip地址
     */
    @JvmStatic
    fun getIpAddress(): String {
        return getLocalIpAddress()
    }

    @JvmStatic
    fun getIpByWifi(): String {

        val wifiManager = ContextProvider.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.connectionInfo?.ipAddress?.let {
            return intToIp(it)
        }
        return ""
    }

    private fun intToIp(i: Int): String {
        val sb = StringBuilder()
        sb.append(i and 0xFF)
        sb.append(".")
        sb.append((i shr 8) and 0xFF)
        sb.append(".")
        sb.append((i shr 16) and 0xFF)
        sb.append(".")
        sb.append((i shr 24) and 0xFF)

        return sb.toString()
    }


    private fun getLocalIpAddress(): String {
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

}