package com.sunny.lib.common.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.telephony.TelephonyManager
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.base.log.SunLog

const val TAG = "SunnyNetWork"

/**
 * Created by zhangxin17 on 2020/8/19
 * 广播监听网络状态——7.0及之后只能动态注册
 */
enum class NetWorkType(val netName: String) {
    AUTO("无状态"),
    CONNECT("已连接"),

    NONE("无网络"),

    OTHER("未知"),

    WIFI("wifi网络"),

    MOBILE("移动网络"),
    MOBILE_2G("2G"),
    MOBILE_3G("3G"),
    MOBILE_4G("4G"),
    MOBILE_OTHER(""),

}

/**
 * 网络模块
 */
class SunNetworkModel {

    init {
        doRegister()
    }

    var networkType = NetWorkType.AUTO
    var mobileNetworkName = ""

    fun isNetworkAvailable(): Boolean {
        return when (networkType) {
            NetWorkType.WIFI, NetWorkType.MOBILE -> true
            else -> false
        }
    }

    private fun doRegister() {
        // 5.0及以后
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            doRegisterNew()
        } else {
            doRegisterOld()
        }
    }

    /**
     * https://www.jianshu.com/p/86d347b2a12b
     * 回调
     */
    private fun doRegisterNew() {
        SunLog.i(TAG, "doRegisterNew")
        val request = NetworkRequest.Builder().build()
        val callback = NetworkCallbackImpl()

        val manager = ContextProvider.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(request, callback)
    }

    /**
     * https://www.sunofbeach.net/a/1224528593531523072
     * 广播
     */
    private fun doRegisterOld() {
        SunLog.i(TAG, "doRegisterOld")
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val receiver = NetworkReceiver()
        ContextProvider.appContext.registerReceiver(receiver, filter)
    }

    /**
     * 处理移动网络类型
     */
    private fun parseNetworkMobileType(subType: Int, subTypeName: String) {
        mobileNetworkName = subTypeName
        when (subType) {
            // 2G
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN //api<8 : replace by 11
            -> {
                SunLog.i(TAG, "connect by mobile 2G")
                networkType = NetWorkType.MOBILE_2G
            }
            //3G
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B, //api<9 : replace by 14
            TelephonyManager.NETWORK_TYPE_EHRPD, //api<11 : replace by 12
            TelephonyManager.NETWORK_TYPE_HSPAP //api<13 : replace by 15
            -> {
                SunLog.i(TAG, "connect by mobile 3G")
                networkType = NetWorkType.MOBILE_3G
            }
            //4G
            TelephonyManager.NETWORK_TYPE_LTE
            -> {
                SunLog.i(TAG, "connect by mobile 4G")
                networkType = NetWorkType.MOBILE_4G
            }
            else -> {
                // 根据名称区分
                when (subTypeName) {
                    "TD-SCDMA", // 移动
                    "WCDMA", // 联通
                    "CDMA2000" // 电信
                    -> {
                        SunLog.i(TAG, "connect by mobile 3G")
                        networkType = NetWorkType.MOBILE_3G
                    }
                    else -> {
                        SunLog.i(TAG, "connect by mobile other type")
                        networkType = NetWorkType.MOBILE_OTHER
                    }
                }
            }
        }
    }

    /**
     * 网络变化广播
     */
    inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val manager = ContextProvider.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo
            networkInfo?.let {
                if (!it.isConnected) {
                    SunLog.i(TAG, "onReceive disconnect")
                    networkType = NetWorkType.NONE
                    return
                }

                when (it.type) {
                    // wifi
                    ConnectivityManager.TYPE_WIFI -> {
                        SunLog.i(TAG, "onReceive connect by wifi")
                        networkType = NetWorkType.WIFI
                    }

                    // mobile
                    ConnectivityManager.TYPE_MOBILE -> {
                        val subType = it.subtype
                        val subTypeName = it.subtypeName
                        SunLog.i(TAG, "onReceive connect by mobile , subType :$subType , subTypeName :$subTypeName")
                        parseNetworkMobileType(subType, subTypeName)
                    }

                    // other
                    else -> {
                        SunLog.i(TAG, "onReceive connect by other model")
                        networkType = NetWorkType.OTHER

                    }
                }
            }
        }
    }

    inner class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            SunLog.i(TAG, "onAvailable")
            networkType = NetWorkType.CONNECT
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            SunLog.i(TAG, "onLost")
            networkType = NetWorkType.NONE
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    SunLog.i(TAG, "connect by wifi")
                    networkType = NetWorkType.WIFI

                } else {
                    SunLog.i(TAG, "connect by mobile")
                    networkType = NetWorkType.MOBILE
                }
            }
        }
    }
}


object SunNetworkUtils {

    /**
     * 网络是否可用
     */
    @JvmStatic
    fun isNetworkAvailable(): Boolean {
        val manager: ConnectivityManager = ContextProvider.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        networkInfo?.let {
            return networkInfo.isConnected
        }
        return false
    }

    /**
     * 获取网络类型
     */
    @JvmStatic
    fun getNetworkType(): Int {
        val manager: ConnectivityManager = ContextProvider.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

}