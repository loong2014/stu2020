package com.sunny.family

/**
 * Created by zhangxin17 on 2020-01-15
 */
class DemoKotlin {

    internal interface PhotoCallback {
        val list: Unit
    }

    private val callback: PhotoCallback = object : PhotoCallback {
        override val list: Unit
            get() {}
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val demo = DemoKotlin()

            demo.testClass()
        }
    }

    class BirdA(val color: String = "color", val age: Int = 1, val des: String = "des")
    class Bird(color: String = "color", age: Int = 1, des: String = "des") {

        val color: String
        val age: Int
        val des: String


        // 自定义构造方法
        constructor(birth:String):this  (des  = birth){

        }

        // lateinit var 不能是基础类型
//        lateinit var count:Int

        lateinit var str:String

        // 构造方法，只能对参数进行赋值
        init {
            this.color = color
            this.age = age
            this.des = des
        }
    }

    fun testClass() {

        val bird1 = Bird(age = 10)

    }

    fun testString() {
        val str = "abcdef"
        val out = str.filter { c -> c in 'a'..'d' }
        println("out :$out") // abcd

        // 原生字符串
        val rawStr = """
            \n hello
            \n kotlin
        """
        print("$rawStr")
    }

    fun testFor() {
        val x = "abc".."xyz"
        println("x = $x")

        println("..")
        for (i in 1..10 step 2) print(i)

        println("downTo 对应10..1的写法")
        for (i in 10 downTo 1 step 2) print(i)

        println("until 半开区间") // 不包含10
        for (i in 1 until 10) print(i)


        val back = "kot" in "abc".."xyz"
        println("back $back")
    }

    fun testFun() {
        val a = 3
        val b = 4
        val demo = DemoKotlin()

        // 方法引用
        demo.get(a, b, demo::sum)

        // lambda函数
        demo.get(a, b, max)

        // 匿名函数
        demo.get(a, b, fun(a: Int, b: Int): Int {
            return a + b
        })

        // lambda风格
        demo.get(a, b, { x, y ->
            x + y
        })

        // 柯里化风格
        demo.get(a, b) { x, y ->
            x + y
        }
    }

    private fun sum(a: Int, b: Int): Int {
        return a + b
    }

    private val max = { x: Int, y: Int -> kotlin.math.max(x, y) }

    private fun name(age: Int) = { name: String -> name }

    fun get(a: Int, b: Int, fu: (Int, Int) -> Int): Int {

        val c = fu(a, b)
        println("根据不同的函数，返回不同的值 :$c")
        return c
    }
}