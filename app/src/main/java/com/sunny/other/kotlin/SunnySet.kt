package com.sunny.other.kotlin

/**
 * Created by zhangxin17 on 2020/12/8
 * 委托类
 */
class SunnySet<T>(private val helper: HashSet<T>) : Set<T> by helper {

    fun sayHello() {
        println("hello world!")
    }

    override fun isEmpty(): Boolean {
        return false
    }
}