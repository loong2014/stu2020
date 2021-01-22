package com.sunny.lib.utils

object AppConfigUtils {

    @JvmStatic
    val pkgName by lazy {
        ContextProvider.appContext.packageName
    }

    @JvmStatic
    fun isDebug(): Boolean {
        return true
    }

    @JvmStatic
    fun isLowCostDevice(): Boolean {
        return false
    }

    /**
     * 当前设备是否属于国广牌照
     */
    @JvmStatic
    fun isCibnBroadcast(): Boolean {
        return true
    }

    /**
     * 当前设备是否属于芒果牌照
     */
    @JvmStatic
    fun isMgtvBroadcast(): Boolean {
        return false
    }
}