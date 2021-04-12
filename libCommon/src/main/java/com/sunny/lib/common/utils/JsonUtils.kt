package com.sunny.lib.common.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object JsonUtils {

    @JvmStatic
    fun getJson(obj: Any?): String? {
        return Gson().toJson(obj)
    }

    @JvmStatic
    fun <T> getBean(str: String): T {
        return Gson().fromJson<T>(str, object : TypeToken<T>() {}.type)
    }

    @JvmStatic
    fun <T> getListBean(str: String): List<T> {
        return Gson().fromJson<List<T>>(str, object : TypeToken<List<T>>() {}.type)
    }

}
