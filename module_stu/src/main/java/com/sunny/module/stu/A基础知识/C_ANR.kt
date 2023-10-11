package com.sunny.module.stu.A基础知识

class C_ANR {

    fun ANR() {
        // 如果在主线程中进行了耗时操作，可能会引发ANR
        /*
        1、启动Activity/Service时，如果 20s 内无法相应(即对应的onCreate()和onResume()/onStartCommand()方法没有执行完毕)
        2、BroadcastReceiver的onReceive()方法没有在 10s 内执行完毕
        3、应用程序在 5s 内无法响应用户的按键/触摸事件时，系统会判定应用程序无响应
        */
        // 如何分析ANR
        /*
        1、获取ANR日志：
            当ANR发生时，Android系统会在/data/anr/traces.txt文件中生成一份报告，这份报告包含了所有线程的堆栈信息。
        2、理解ANR日志：
            ANR日志通常很长，因为它包含了所有线程的堆栈信息。但是，你应该主要关注"main"线程，因为这是导致ANR的原因。
            在"main"线程的堆栈信息中，你应该找到你的应用程序的代码，并查看哪些方法在执行。
        3、分析问题：
            一旦你找到了导致ANR的代码，你就需要理解为什么这些代码会导致ANR。
            这通常涉及到一些耗时的操作，如网络访问、大量的计算、磁盘I/O等。你需要找到这些耗时操作，并考虑如何将它们移动到后台线程执行。
        4、修复问题：
            修复ANR通常涉及到将耗时操作从主线程移动到后台线程。
            在Android中，有多种方式可以做到这一点，包括使用Handler、AsyncTask、ThreadPoolExecutor、Kotlin Coroutines等。
        5、验证修复：
            一旦你修复了问题，你需要验证你的修复是否有效。
            你可以通过重现ANR来验证，或者使用一些性能分析工具，如Traceview或Systrace，来验证你的代码是否还在主线程中执行耗时操作。
         */
    }
}