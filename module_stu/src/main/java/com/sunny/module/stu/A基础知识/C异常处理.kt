package com.sunny.module.stu.A基础知识

import android.content.ActivityNotFoundException
import android.database.sqlite.SQLiteException
import android.util.Log
import java.io.FileNotFoundException

class C异常处理 {

    // 主要分为 Error 和 Exception, 都是 Throwable 的子类
    fun 异常分类() {

        /*
        Error表示的是严重的问题，它们在 Java 应用程序的正常运行中不应该被捕获。

        Error 通常由 JVM 抛出，并指示一些严重的问题，比如 OutOfMemoryError、StackOverflowError 等，
        这些错误通常是 JVM 无法恢复的。一般来说，开发者不应尝试捕获这类错误。
         */
        var error: Error? = null

        /*
        Exception 类表示的是程序可以处理的问题，它们应该被捕获并以某种方式处理。

        Exception 可以分为两类: 检查异常（checked exceptions）和非检查异常（unchecked exceptions）。
            检查异常必须被显式捕获或者在方法签名中声明，如 IOException；
            非检查异常则无需显式捕获，如 NullPointerException。

        在 Kotlin 中，没有检查异常的概念，所有的异常都是非检查异常。
         */
        var exception: Exception? = null

        // 运行时异常, 程序在运行过程中出现的异常
        exception = RuntimeException()

        // 当尝试访问一个为 null 的对象的成员时，会抛出此异常。
        // 在 Kotlin 中，由于其内置的空安全特性，这种异常的可能性大大减少，
        // 但在某些情况下仍然可能发生（例如，当使用 `!!` 操作符强制访问一个可能为 null 的对象时）。
        exception = NullPointerException()

        // 当方法接收到的参数不符合其预期的值时，会抛出此异常。
        exception = IllegalArgumentException()

        // 当对象的内部状态不符合方法调用的预期时，会抛出此异常。
        exception = IllegalStateException()

        // 当尝试访问数组或列表的一个不存在的索引时，会抛出此异常。
        exception = ArrayIndexOutOfBoundsException()

        // 当尝试将对象强制转换为不兼容的类型时，会抛出此异常。
        exception = ClassCastException()

        // 当尝试启动一个不存在的 Activity 时，会抛出此异常。
        exception = ActivityNotFoundException()

        // 当尝试访问一个不存在的文件时，会抛出此异常。
        exception = FileNotFoundException()

        // 当尝试执行 SQLite 数据库操作但操作失败时，会抛出此异常。
        exception = SQLiteException()

        // 当应用程序尝试分配内存但系统内存不足时，会抛出此错误。
        error = OutOfMemoryError()

        // 当应用程序递归过深而导致堆栈溢出时抛出。
        error = StackOverflowError()

        // ANR（Application Not Responding）
        // 当应用程序在主线程上执行了耗时操作，导致无法响应用户输入或者无法在5秒内完成屏幕更新时，系统会显示 ANR 对话框。

    }

    fun 抛出异常() {
        // 抛出异常（Throwing an Exception）
        /*
        你可以使用 throw 关键字来抛出异常。
        例如：throw IllegalArgumentException("Invalid argument")。
         */
    }

    fun 捕获异常() {
        // 捕获异常（Catching an Exception）
        /*
        你可以使用 try-catch 块来捕获并处理异常。
            try {
                // 在 try 块中的代码是可能会抛出异常的代码，
            } catch (e: ExceptionType) {
                // 在 catch 块中的代码是处理异常的代码。
            } finally {
                // 无论是否发生异常，finally 块中的代码都会被执行
                // 我们在 finally 块中进行资源清理工作，如关闭打开的文件或数据库连接等。
            }
         */
    }

    fun 捕获未处理的异常() {

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        val newHandler = Thread.UncaughtExceptionHandler { t, e ->
            // 捕获到异常后，先自己处理（比如保存crash信息），然后再将异常抛出
            defaultHandler?.uncaughtException(t, e)
        }
        Thread.setDefaultUncaughtExceptionHandler(newHandler)
    }

    /**
     * 打印当前方法的调用栈
     */
    fun debugStackTrack(tag: String) {
        val array = Thread.currentThread().stackTrace
        val sb = StringBuilder()
        sb.append("\n======>begin")
        for (ele in array) {
            sb.append("\n===>")
            sb.append(ele.toString())
        }
        sb.append("\n======>end\n")
        Log.e(tag, sb.toString())
    }
}
