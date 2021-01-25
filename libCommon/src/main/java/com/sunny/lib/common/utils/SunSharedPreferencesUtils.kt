package com.sunny.lib.common.utils

import android.content.Context
import android.content.SharedPreferences
import com.sunny.lib.utils.AppConfigUtils
import com.sunny.lib.utils.ContextProvider

/**
 * SP本地数据存储工具
 */
object SunSharedPreferencesUtils {

    private val defSp: SharedPreferences by lazy {
        val defSpName = "sp_" + AppConfigUtils.pkgName
        ContextProvider.appContext.getSharedPreferences(defSpName, Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun putString(key: String, value: String) {
        defSp.edit().putString(key, value).apply()
    }

    @JvmStatic
    fun getString(key: String, defValue: String?): String? {
        return defSp.getString(key, defValue)
    }

    @JvmStatic
    fun putInt(key: String, value: Int) {
        defSp.edit().putInt(key, value).apply()
    }

    @JvmStatic
    fun getInt(key: String, defValue: Int): Int {
        return defSp.getInt(key, defValue)
    }

    @JvmStatic
    fun putLong(key: String, value: Long) {
        defSp.edit().putLong(key, value).apply()
    }

    @JvmStatic
    fun getLong(key: String, defValue: Long): Long {
        return defSp.getLong(key, defValue)
    }

    @JvmStatic
    fun putBoolean(key: String, value: Boolean) {
        defSp.edit().putBoolean(key, value).apply()
    }

    @JvmStatic
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return defSp.getBoolean(key, defValue)
    }

    @JvmStatic
    fun remove(key: String) {
        defSp.edit().remove(key).apply()
    }

}