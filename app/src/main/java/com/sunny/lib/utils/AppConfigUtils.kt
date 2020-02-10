package com.sunny.lib.utils

object AppConfigUtils {

    val pkgName by lazy {
        ContextProvider.appContext.packageName
    }

    fun isLowCostDevice(): Boolean {
        return false
    }
}