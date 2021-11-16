package com.sunny.lib.common.thread

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object SunThreadPool {

    private val SIZE = 1
    private val MAX_SIZE = 2
    private var mPool: ThreadPoolExecutor? = null

    @JvmStatic
    fun getGlobalSingleThreadPoolInstance(): ThreadPoolExecutor? {
        if (mPool == null) {
            synchronized(SunThreadPool::class.java) {
                if (mPool == null) {
                    mPool = ThreadPoolExecutor(SIZE, MAX_SIZE, 3,
                            TimeUnit.SECONDS,
                            LinkedBlockingQueue())
                }
            }
        }
        return mPool
    }

}