package com.sunny.module.stu.A基础知识

import android.app.Activity
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

class BHandler机制 {

    fun stu_消息处理机制() {
        // 用于在应用程序的不同【线程】之间传递和处理消息和 Runnable 对象
        /*
        Handler：用于发送和处理消息的主要组件。当你创建一个 Handler 对象时，它会与当前线程的 Looper 相关联。
            你可以通过 Handler 发送 Message 对象或 Runnable 对象到 MessageQueue，也可以在 Handler 中处理来自 MessageQueue 的消息。

        Looper：是每个线程的消息循环。它从 MessageQueue 中取出 Message 或 Runnable 对象，并将它们分发到对应的 Handler 进行处理。
            在 Android 中，主线程默认会创建 Looper，但是子线程需要手动创建。

        MessageQueue：是一个消息队列，用于存储所有通过 Handler 发送的 Message 或 Runnable 对象。
            Looper 会按照消息的入队顺序（或者定时消息的执行时间）从 MessageQueue 中取出消息。

        Message：是在线程之间传递的消息对象。
         */


        // 创建一个工作线程
        val workHandler: Handler by lazy {
            val ht = HandlerThread("worker")
            ht.start()
            Handler(ht.looper)
        }

        val mCallback = object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                // 2、处理消息
                return false
            }
        }
        // 创建一个主线程
        val mainHandler: Handler = object : Handler(Looper.getMainLooper(), mCallback) {

            // 在Looper中调用Handler的dispatchMessage处理消息
            override fun dispatchMessage(msg: Message) {
//                if (msg.callback != null) {
//                    // 1、当消息是Runnable，则直接执行
//                    msg.callback.run()
//                } else {
//                    if (mCallback != null) {
//                    // 2、当Handler有Callback时，先交给Callback处理
//                        if (mCallback.handleMessage(msg)) {
//                            return
//                        }
//                    }
//                    // 3、交给Handler处理消息
//                    handleMessage(msg)
//                }
                super.dispatchMessage(msg)
            }

            override fun handleMessage(msg: Message) {
                // 3、处理消息
                super.handleMessage(msg)
            }
        }

        mainHandler.sendEmptyMessage(1)
        mainHandler.post(Runnable {
            // do something
        })
        mainHandler.post { }

    }

    fun stu_Message() {
        // 是在线程之间传递的消息对象
        /*
        what(Int)：消息id
        arg1(Int)：额外字段
        arg2(Int)：额外字段
        obj(Any/Object)：附带的任意数据对象
        data(Bundle)：用于传递复杂的数据
        callback(Runnable)：消息对应的Runnable

        target(Handler)：消息发送者
         */
    }

    fun stu_同步屏障() {
        // 阻止消息队列中的任何异步消息被派发，直到这个屏障被移除。这是一种可以暂时阻止消息处理以便进行一些特殊操作的机制。
        // 在 Android 的 MessageQueue 中，消息分为同步消息和异步消息两种。
        val msg = Message.obtain()
        msg.isAsynchronous = true // 设置消息为异步消息

        // 开启/关闭同步屏障
        /*
        val token= messageQueue.postSyncBarrier()
        添加一条msg.target=null的消息，当通过queue.next获取消息时，如果最近的msg.target=null，则获取最近的一条异步消息

        messageQueue.removeSyncBarrier(token)
        删除token对应的消息
         */
    }

    fun stu_内存泄漏(activity: Activity) {

        /*
        在Android开发中，Handler 有时会导致内存泄漏。这是因为在 Handler 的内部实现中，Handler 会持有一个对 MessageQueue 的引用，
            而 MessageQueue 是 Looper 的一部分，Looper 又是线程的一部分。
        如果你在 Activity 中创建了一个 Handler，并向其发送了一些延迟的消息，那么即使 Activity 已经结束，
            这些消息仍然在 MessageQueue 中，因此 Handler、Looper 和线程都不能被垃圾回收，从而导致内存泄漏。
         */
        // 解决方案
        // 1、静态内部类
        class MyHandler(act: Activity) : Handler() {
            private val weakRef = WeakReference(act)

            override fun dispatchMessage(msg: Message) {
                val startTime = System.currentTimeMillis()
                super.dispatchMessage(msg)
                val endTime = System.currentTimeMillis()
                // 计算Message耗时
                val useTime = endTime - startTime

            }

            override fun handleMessage(msg: Message) {
                if (weakRef.get() != null) {
                    // do something with the message
                }
            }
        }

        val handler = MyHandler(activity)

        // 当Activity销毁时，移除所有消息
        handler.removeCallbacksAndMessages(null)

        // 2、借助ViewModel和LiveData来更新UI，因为LiveData对象会自动处理生命周期问题
    }
}