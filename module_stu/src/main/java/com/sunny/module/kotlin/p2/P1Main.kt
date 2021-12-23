package com.sunny.module.kotlin.p2

class P1Main {


    fun doFun1() {
//        val p1Aaa = P1Aaa("zhang")// error
        val p1Aaa = P1Aaa.build("zhang")
    }

    /**
     * 分别用 Array、IntArray、List 实现 「保存 1-100_000 的数字，并求出这些数字的平均值」，
     * 打印出这三种数据结构的执行时间。
     */
    fun doFun21() {

        val array = arrayOf<Int>(100000)
        var sum: Int = 0
        val t1 = System.currentTimeMillis()
        for (i in 1..100000) {
            array[i - 1] = i
            sum += i
        }
        val t2 = System.currentTimeMillis()
        System.out.println("use time :${t2 - t1}")
        val pre = sum / 100000
        System.out.println("pre :$pre")

    }

    fun doFun22() {

        val array = intArrayOf(100000)
        var sum: Int = 0
        val t1 = System.currentTimeMillis()
        for (i in 1..100000) {
            array[i - 1] = i
            sum += i
        }
        val t2 = System.currentTimeMillis()
        System.out.println("use time :${t2 - t1}")
        val pre = sum / 100000
        System.out.println("pre :$pre")
    }

    fun doFun23() {

        val array = mutableListOf<Int>(100000)
        var sum: Int = 0
        val t1 = System.currentTimeMillis()
        for (i in 1..100000) {
            array[i - 1] = i
            sum += i
        }
        val t2 = System.currentTimeMillis()
        System.out.println("use time :${t2 - t1}")
        val pre = sum / 100000
        System.out.println("pre :$pre")
    }
}

fun main(args: Array<String>) {
    val main = P1Main()
    main.doFun21()
}