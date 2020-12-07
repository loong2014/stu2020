package com.sunny.family.extend

/**
 * Created by zhangxin17 on 2020/11/27
 */
fun main() {
    val str = "adaf34adfar54af"
    val count = str.lettersCount()

    val c = str.indexOfChar('c')
}

/**
 * 扩展函数：可以给任意类添加扩展函数
 * fun ClassName.methodName(param1:Int,param2:Int):Int{
 * return 0
 * }
 */
fun String.lettersCount(): Int {
    var count = 0
    for (char in this) {
        if (char.isLetter()) {
            count++
        }
    }
    return count
}

/**
 * 扩展+重载
 */
operator fun String.times(count: Int): String {
    val sb = StringBuilder()
    repeat(count) {
        sb.append(this)
    }
    return sb.toString()
}

/**
 * 是否包含字符 char，并返回字符的位置
 */
fun String.indexOfChar(char: Char): Int {
    var index = 0
    for (c in this) {
        if (char == c) {
            return index
        }
        index++
    }
    return -1
}