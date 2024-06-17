package com.sunny.module.view

object DebugHelper {

    val cacheMap = mutableMapOf<String, MutableMap<String, String>>()


    fun getMap(ffid: String): MutableMap<String, String> {
        return cacheMap[ffid] ?: buildMap(ffid)
    }

    private fun buildMap(ffid: String): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map["ffid"] = ffid
        return map
    }

    fun put(ffid: String, key: String, value: String) {
        getMap(ffid)[key] = value
    }

    fun get(ffid: String, key: String): String {
        return getMap(ffid)[key] ?: "None"
    }
}