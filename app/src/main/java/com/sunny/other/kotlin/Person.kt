package com.sunny.other.kotlin


/**
 * 默认没有open 关键字，表示类是final的，不可被继承
 */
open class Person(val name: String, val age: Int) {

    init {
        println("Person init")
    }

    open fun show() {
        println("$name 今年 $age 岁")
    }
}