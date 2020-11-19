package com.sunny.other.kotlin

/**
 * name和age不能声明为val，因为在主构造函数中声明成val/var的参数，将自动成为该类的字断，会造成与父类中同名字断冲突。
 * 因此在主构造函数中不加任何什么，使其作用域仅限定在主构造函数中
 */
class Student(val grade: Int = 0, name: String, age: Int) : Person(name, age), Study {

    var sex: Boolean = false

    /**
     * 主构造函数方法处理
     */
    init {
        println("Student init")
    }

    constructor(grade: Int, sex: Boolean) : this(grade, "xxx", 11) {
        this.sex = sex
    }

    override fun show() {
        println("$name 今年 $age 岁 ${
            if (sex) {
                "男"
            } else {
                "女"
            }
        }, $grade 年级")
    }

    override fun readBooks() {
        println("$name need reading")
    }

    override fun doHomework() {
        println("$name is doing homework")
    }

}