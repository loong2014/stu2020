package com.sunny.other.kotlin

/**
 * Created by zhangxin17 on 2020/11/24
 */
fun main() {
    // 1
//    val stu = Student(name = "aaa", age = 11)
////    val stu = Student(1,true)
//    stu.show()

    // 2
    val p1 = Phone("12345", "zhang")
//    val p2 = Phone("12345", "zhang")
//
//    println("p1($p1) equal p2($p2) : ${p1 == p2}")

    // 3
    val list = listOf("aaa", "bbbb", "ccc", "ddddd", "ff")

    val lambda = { str: String -> str.length }
    val maxLen = list.maxBy(lambda)
    // 简化===>
    val maxLen2 = list.maxBy { it.length }

    println("maxLen :$maxLen")
}
