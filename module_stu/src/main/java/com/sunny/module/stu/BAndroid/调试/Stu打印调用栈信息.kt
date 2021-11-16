package com.sunny.module.stu.BAndroid.调试

import android.util.Log
import com.sunny.module.stu.base.StuImpl

class Stu打印调用栈信息 : StuImpl() {

    override fun b_作用() {
        // 通过打印方法调用栈，追溯方法的调用轨迹

        //
        Log.getStackTraceString(Throwable())

        //
        val map: Map<Thread, Array<StackTraceElement>> = Thread.getAllStackTraces()

        //
        var elementArray: Array<StackTraceElement> = Thread.currentThread().stackTrace

        // print
        val printSB = StringBuilder()
        elementArray.forEach { ele: StackTraceElement ->
            ele.run {
                printSB.append(toString())
            }
        }

    }

    fun 异常() {
        //
        Log.getStackTraceString(Throwable())
    }

    fun 调用栈() {
        // 获取所有调用栈
        val map: Map<Thread, Array<StackTraceElement>> = Thread.getAllStackTraces()

        // 当前线程调用栈
        var elementArray: Array<StackTraceElement> = Thread.currentThread().stackTrace

        // print
        val printSB = StringBuilder()
        elementArray.forEach { ele: StackTraceElement ->
            ele.run {
                printSB.append(toString())
            }
        }
    }
}