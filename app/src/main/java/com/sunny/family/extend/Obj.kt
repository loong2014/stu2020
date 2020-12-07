package com.sunny.family.extend

/**
 * Created by zhangxin17 on 2020/11/27
 * 运算符重载，实现两个对象的加减
 */
class Obj {

    operator fun plus(obj: Obj): Obj {
        return this
    }
}

/**
 *
 */
class Money(val value: Int) {
    operator fun plus(money: Money): Money {
        val sum = value + money.value
        return Money(sum)
    }

    operator fun plus(newValue: Int): Money {
        return Money(value + newValue)
    }
}