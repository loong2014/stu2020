package com.sunny.lib.common.thread

import java.util.concurrent.*

/**
 * https://www.jianshu.com/p/8363d023be1a
 *
 * https://www.jianshu.com/p/cfa1d48d2d9f
 */

const val PoolTypeCustom = 0
const val PoolTypeFixed = 1
const val PoolTypeCached = 2
const val PoolTypeSingle = 3
const val PoolTypeScheduled = 4

class StuThreadPool(poolType: Int = PoolTypeCustom) {


    val threadPool: ExecutorService by lazy {
        getExecutorService(poolType)
    }


    private fun getExecutorService(type: Int): ExecutorService {
        return when (type) {
            PoolTypeFixed -> {
                Executors.newFixedThreadPool(1)
            }
            PoolTypeCached -> {
                Executors.newCachedThreadPool()
            }
            PoolTypeSingle -> {
                Executors.newSingleThreadExecutor()
            }
            PoolTypeScheduled -> {
                Executors.newScheduledThreadPool(2)
            }
            else -> {
                ThreadPoolExecutor(
                        getCorePoolSize(),
                        getMaximumPoolSize(),
                        1L, TimeUnit.SECONDS,
                        buildWorkQueue(),
                        buildThreadFactory()
                )
            }
        }
    }

    /**
     * 获取线程池的 核心线程数 最大值，默认在空闲状态下，线程也不会被释放
     *
     */
    private fun getCorePoolSize(): Int {
        return 2
    }

    /**
     * 获取线程池的 最大限度线程数 大小
     *
     * CPU 密集型任务(N+1)
     *      这种任务消耗的主要是 CPU 资源，可以将线程数设置为 N（CPU 核心数）+1，
     *      比 CPU 核心数多出来的一个线程是为了防止线程偶发的缺页中断，或者其它原因导致的任务暂停而带来的影响。
     *      一旦任务暂停，CPU 就会处于空闲状态，而在这种情况下多出来的一个线程就可以充分利用 CPU 的空闲时间。
     *
     * I/O 密集型任务(2N)
     *      这种任务应用起来，系统会用大部分的时间来处理 I/O 交互，而线程在处理 I/O 的时间段内不会占用 CPU 来处理，
     *      这时就可以将 CPU 交出给其它线程使用。
     *      因此在 I/O 密集型任务的应用中，我们可以多配置一些线程，具体的计算方法是 2N。
     *
     * 如何判断是 CPU 密集任务还是 IO 密集任务？
     *      CPU 密集型简单理解就是利用 CPU 计算能力的任务比如你在内存中对大量数据进行排序。
     *      单凡涉及到网络读取，文件读取这类都是 IO 密集型，
     *      这类任务的特点是 CPU 计算耗费时间相比于等待 IO 操作完成的时间来说很少，大部分时间都花在了等待 IO 操作完成上。
     */
    private fun getMaximumPoolSize(): Int {
        return 10
    }

    /**
     * 构建存放任务的阻塞队列
     */
    private fun buildWorkQueue(capacity: Int = Int.MAX_VALUE): BlockingQueue<Runnable> {
        return LinkedBlockingQueue<Runnable>(capacity)
    }

    /**
     * 构建创建线程的工厂，可以给创建的线程设置有意义的名字，可方便排查问题。
     */
    private fun buildThreadFactory(): ThreadFactory {
        return CommonThreadFactory("stu")
    }
}

fun main() {

    println("Main >> StuThreadPool")
    val stuThreadPool = StuThreadPool(PoolTypeCached)


    for (num in 1..100) {
        stuThreadPool.threadPool.execute {
            Thread.sleep(100)
            println("Main >> num :$num")
        }
    }

}