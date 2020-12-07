package com.sunny.other.kotlin

import kotlin.reflect.KProperty

/**
 * Created by zhangxin17 on 2020/12/8
 */
class Delegate {
    var propValue: Any? = null


    operator fun getValue(sunnyClass: SunnyClass, property: KProperty<*>): Any? {
        println("Delegate  getValue")
        return propValue
    }

    operator fun setValue(sunnyClass: SunnyClass, property: KProperty<*>, value: Any?) {
        println("Delegate  setValue")
        propValue = value
    }


}