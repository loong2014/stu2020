package com.sunny.other.kotlin

object Singleton {

    val fruits by lazy {
        listOf("", "", "") // 不可变集合，只能读取
        mutableListOf("", "", "") // 可变集合
        mutableMapOf("aa" to 1, "bb" to 2) // key to value
    }

}