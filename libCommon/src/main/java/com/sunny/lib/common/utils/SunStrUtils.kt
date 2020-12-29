package com.sunny.lib.common.utils

import android.text.TextUtils

object SunStrUtils {

    fun strToInt(str: String, def: Int): Int {
        var str = str
        if (TextUtils.isEmpty(str)) {
            return def
        }
        str = str.trim { it <= ' ' }
        return if (TextUtils.isDigitsOnly(str)) {
            str.toInt()
        } else def
    }

    fun strToLong(str: String, def: Long): Long {
        var str = str
        if (TextUtils.isEmpty(str)) {
            return def
        }
        str = str.trim { it <= ' ' }
        return if (TextUtils.isDigitsOnly(str)) {
            str.toLong()
        } else def
    }

    fun rebuildString(str: String, maxLen: Int): String? {
        var str = str
        if (TextUtils.isEmpty(str)) {
            str = ""
        } else {
            if (str.length > maxLen) {
                str = str.substring(0, maxLen) + "..."
            }
        }
        return str
    }

    /**
     * 检查字符串是否为空
     */
    fun isEmpty(string: String?): Boolean {
        return !(string != null && string.isNotEmpty())
    }

    fun isArrayEmpty(objs: Array<Any?>?): Boolean {
        if (objs == null || objs.isEmpty()) {
            return true
        }
        var isEmpty = true
        for (obj in objs) {
            if (obj != null) {
                isEmpty = false
                break
            }
        }
        return isEmpty
    }

    fun isBlank(str: String?): Boolean {
        return str.isNullOrBlank()
    }

    fun equalsNull(str: String): Boolean {
        return isBlank(str) || str.equals("null", ignoreCase = true)
    }

}