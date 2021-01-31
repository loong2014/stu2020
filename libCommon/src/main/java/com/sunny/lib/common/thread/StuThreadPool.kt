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

    /**
     * RUNNING
     * SHUTDOWN
     * STOP
     * TIDYING
     * TERMINATED
     */
    val threadPool: ExecutorService by lazy {
        getExecutorService(poolType)
    }

    /**
     * RUNNING --> SHUTDOWN
     * 1-该状态的线程池不会接收新任务
     * 2-但会处理阻塞队列中的任务
     *
     * SHUTDOWN --> TIDYING
     * 队列为空，并且线程池中执行的任务也为空,进入TIDYING状态;
     */
    fun shutdown() {
        threadPool.shutdown()
    }

    /**
     * RUNNING --> STOP
     * 1-该状态的线程不会接收新任务
     * 2-也不会处理阻塞队列中的任务
     * 3-而且会中断正在运行的任务
     *
     * STOP -->TIDYING
     * 线程池中执行的任务为空,进入TIDYING状态
     */
    fun shutdownNow() {
        // 返回阻塞队列中未执行的任务
        val list: List<Runnable>? = threadPool.shutdownNow()
        println("Main >> shutdownNow lave task size :${list?.size}")
    }

    /**
     * 判断线程是否处于终止状态，即不再接受新的任务
     */
    fun isShutdown(): Boolean {
        return threadPool.isShutdown
    }

    /**
     * 判断所有任务是否都已经完成，前提是调用了[shutdown]或者[shutdownNow]。否则返回的永远是false
     */
    fun isTerminated(): Boolean {
        return threadPool.isTerminated
    }

    /**
     * 延时判断所有任务是否都已经完成，前提是调用了[shutdown]或者[shutdownNow]。否则返回的永远是false
     *
     * 调用[awaitTermination]后，调用线程会处于一种阻塞的状态
     * 如果没有调用[shutdown]或者[shutdownNow]，直接调用[awaitTermination]，则线程会一直处于阻塞状态，直到超时，并且返回false
     *
     * 当线程池中任务全部完成或者超过[awaitTermination]设置的等待时长，则返回一个Boolean值：
     *      true：表示线程池中的任务都执行完毕
     *      false：表示线程池中任务还在执行中
     *
     * 等待结束时机：
     * 1-线程池中任务执行完毕，返回true
     * 3-等待的时间超过指定的时间，且线程池中没有正在允许的任务，返回true
     * 2-等待的时间超过指定的时间，且线程池中有任务正在运行，返回false
     */
    fun awaitTermination(millis: Long): Boolean {
        val result = threadPool.awaitTermination(millis, TimeUnit.MILLISECONDS)

        println("Main >> awaitTermination  result :$result")
        return result
    }

    /**
     * 往线程池中添加一个任务
     */
    fun execute(command: () -> Unit) {
        threadPool.execute(command)
    }

    private fun getExecutorService(type: Int): ExecutorService {
        return when (type) {
            PoolTypeFixed -> {
                /**
                 * 固定数目线程的线程池
                 *
                 *
                 * 线程池特点：
                 * 核心线程数和最大线程数大小一样
                 * 没有所谓的非空闲时间，即keepAliveTime为0
                 * 阻塞队列为无界队列[LinkedBlockingQueue]
                 *
                 * 工作机制：
                 * 1-如果线程数少于核心线程，创建核心线程执行任务
                 * 2-如果线程数等于核心线程，把任务添加到LinkedBlockingQueue阻塞队列
                 * 3-如果线程执行完任务，去阻塞队列取任务，继续执行
                 *
                 * 风险：
                 * 如果线程获取一个任务后，任务的执行时间比较长(比如文件读取等IO操作)，
                 * 会导致队列的任务越积越多，导致机器内存使用不停飙升，最终导致OOM
                 *
                 * 使用场景：
                 * 适用于处理CPU密集型的任务，确保CPU在长期被工作线程使用的情况下，尽可能的少的分配线程，即适用执行长期的任务
                 *
                 * [LinkedBlockingQueue]
                 * [ThreadPoolExecutor.execute]
                 */
                Executors.newFixedThreadPool(2)
            }

            PoolTypeCached -> {
                /**
                 * 可缓存线程的线程池
                 *
                 *
                 * 线程池特点：
                 * 核心线程数为0
                 * 最大线程数为Integer.MAX_VALUE
                 * 阻塞队列是[SynchronousQueue]
                 * 非核心线程空闲存活时间为60秒
                 *
                 * 工作机制：
                 * 1-因为没有核心线程，所以任务直接加到SynchronousQueue队列
                 * 2-判断是否有空闲线程，如果有，就去取出任务执行
                 * 3-如果没有空闲线程，就新建一个线程执行。
                 * 4-执行完任务的线程，还可以存活60秒，如果在这期间，接到任务，可以继续活下去；否则，被销毁
                 *
                 * 风险：
                 * 当提交任务的速度大于处理任务的速度时，每次提交一个任务，就必然会创建一个线程
                 * 极端情况下会创建过多的线程，耗尽CPU和内存资源
                 *
                 * 使用场景：
                 * 用于并发执行大量短期的小任务。由于空闲 60 秒的线程会被终止，
                 * 长时间保持空闲的 CachedThreadPool 不会占用任何资源。
                 *
                 * [SynchronousQueue]
                 * [ThreadPoolExecutor.execute]
                 */
                Executors.newCachedThreadPool()
            }
            PoolTypeSingle -> {
                /**
                 * 单线程的线程池
                 *
                 *
                 * 线程池特点：
                 * 核心线程数为1
                 * 最大线程数也为1
                 * 阻塞队列是[LinkedBlockingQueue]
                 * keepAliveTime为0
                 *
                 * 工作机制：
                 *
                 * 1-线程池是否有一条线程在，如果没有，新建线程执行任务
                 * 2-如果有，将任务加到阻塞队列
                 * 3-当前的唯一线程，从队列取任务，执行完一个，再继续取，一个人（一条线程）夜以继日地干活。
                 *
                 * 风险：
                 * 如果线程获取一个任务后，任务的执行时间比较长(比如文件读取等IO操作)，
                 * 会导致队列的任务越积越多，导致机器内存使用不停飙升，最终导致OOM
                 *
                 * 使用场景：
                 * 适用于串行执行任务的场景，一个任务一个任务地执行。
                 *
                 * [LinkedBlockingQueue]
                 * [ThreadPoolExecutor.execute]
                 */
                Executors.newSingleThreadExecutor()
            }
            PoolTypeScheduled -> {
                /**
                 * 定时及周期执行的线程池
                 *
                 *
                 * 线程池特点：
                 * 最大线程数为Integer.MAX_VALUE
                 * 阻塞队列DelayedWorkQueue
                 * keepAliveTime为0
                 * [ScheduledThreadPoolExecutor.scheduleAtFixedRate] ：按某种速率周期执行
                 * [ScheduledThreadPoolExecutor.scheduleWithFixedDelay]：在某个延迟后执行
                 *
                 * 工作机制：
                 * 1-添加一个任务
                 * 2-线程池中的线程从 DelayQueue 中取任务
                 * 3-线程从 DelayQueue 中获取 time 大于等于当前时间的task
                 * 4-执行完后修改这个 task 的 time 为下次被执行的时间
                 * 5-将这个 task 放回DelayQueue队列中
                 *
                 *[ScheduledThreadPoolExecutor.execute]
                 */
                Executors.newScheduledThreadPool(1)
            }
            else -> {

                /**
                 * [ThreadPoolExecutor.execute]
                 * [ThreadPoolExecutor.getTask]
                 *
                 */
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
     *
     * 添加：workQueue.offer
     * 移除：workQueue.remove
     *
     * 获取：
     *  boolean timed = allowCoreThreadTimeOut || wc > corePoolSize; // true，表示线程空闲时间超时后需要被释放
     *  Runnable r = timed ?
     *      workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) // 如果队列为空，则阻塞等待，超时后返回null
     *  :
     *      workQueue.take(); // 如果队列为空，则一直阻塞等待，有数据后返回
     */
    private fun buildWorkQueue(type: Int = -1, capacity: Int = Int.MAX_VALUE): BlockingQueue<Runnable> {

        /**
         * [ThreadPoolExecutor.execute]
         *
         */
        return when (type) {
            1 -> {
                /**
                 * https://blog.csdn.net/qq_31865983/article/details/105370688
                 * 有界队列，是一个用 数组 实现的有界阻塞队列，按FIFO排序量
                 *
                 * putIndex：待添加task的下标
                 * takeIndex：待获取task的下标
                 *
                 * 添加：[ArrayBlockingQueue.offer]
                 * 移除：[ArrayBlockingQueue.remove]
                 * 获取：[ArrayBlockingQueue.take]
                 * 获取：[ArrayBlockingQueue.poll]
                 */
                ArrayBlockingQueue(capacity)
            }
            2 -> {
                /**
                 * 可设置容量队列,基于 链表 结构的阻塞队列，按FIFO排序任务.
                 * 容量可以选择进行设置，不设置的话，将是一个无边界的阻塞队列，最大长度为Integer.MAX_VALUE，
                 * 吞吐量通常要高于ArrayBlockingQuene；newFixedThreadPool线程池使用了这个队列
                 * head：栈头
                 * last：栈尾
                 *
                 * [Executors.newSingleThreadExecutor]
                 * [Executors.newFixedThreadPool]
                 *
                 * 添加：[LinkedBlockingQueue.offer]
                 * 移除：[LinkedBlockingQueue.remove]
                 * 获取：[LinkedBlockingQueue.take]
                 * 获取：[LinkedBlockingQueue.poll]
                 */
                LinkedBlockingQueue<Runnable>()
            }
            3 -> {
                /**
                 * 优先级队列，是具有优先级的无界阻塞队列
                 * 其数据结构是 数组
                 *
                 * 添加：[PriorityBlockingQueue.offer]
                 * 移除：[PriorityBlockingQueue.remove]
                 * 获取：[PriorityBlockingQueue.take]
                 * 获取：[PriorityBlockingQueue.poll]
                 */
                PriorityBlockingQueue()
            }
            4 -> {
                /**
                 * 同步队列，一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，
                 * 否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQuene
                 *
                 * [Executors.newCachedThreadPool]
                 *
                 * 添加：[SynchronousQueue.offer]
                 * 移除：[SynchronousQueue.remove]
                 * 获取：[SynchronousQueue.take]
                 * 获取：[SynchronousQueue.poll]
                 */
                SynchronousQueue<Runnable>()
            }
            else -> {
                LinkedBlockingQueue<Runnable>(capacity)
            }
        }
    }

    /**
     * 构建创建线程的工厂，可以给创建的线程设置有意义的名字，可方便排查问题。
     */
    private fun buildThreadFactory(): ThreadFactory {
        return CommonThreadFactory("stu")
    }
}

object TestDelayQueue {

    /**
     * https://www.cnblogs.com/myseries/p/10944211.html
     *
     * DelayQueue属于排序队列，它的特殊之处在于队列的元素必须实现Delayed接口，该接口需要实现compareTo和getDelay方法。
     * 其数据结构是一个优先队列:PriorityQueue，其具体实现是 数组，初始大小是11
     * 在线程池[Executors.newScheduledThreadPool]中，使用的就是自定义的延时队列 [ScheduledThreadPoolExecutor.getQueue]
     * 是一个任务定时周期的延迟执行的队列。根据指定的执行时间从小到大排序，否则根据插入到队列的先后排序。
     *
     * 添加：[DelayQueue.offer]
     * 删除：[DelayQueue.remove]
     *
     * 获取：[DelayQueue.take]
     * 获取：[DelayQueue.poll]
     */
    private fun getDelayQueue(): DelayQueue<SunDelayTask> {
        return DelayQueue<SunDelayTask>()
    }

    private fun buildDelayTask(time: Long): SunDelayTask {
        return SunDelayTask(time, Runnable {
            println("testDelayQueue >> ${System.currentTimeMillis()} , time :$time")
        })
    }

    fun doTest() {
        val delayQueue = getDelayQueue()
        kotlin.run {
            delayQueue.offer(buildDelayTask(1000))
            delayQueue.offer(buildDelayTask(500))
            delayQueue.offer(buildDelayTask(4000))
            delayQueue.offer(buildDelayTask(2600))
        }

        println("TestDelayQueue >> start")
        while (!delayQueue.isEmpty()) {
            val task = delayQueue.take()
            task.doRun()
        }
        println("TestDelayQueue >> end")
    }

    /**
     * 延时队列的item
     */
    class SunDelayTask(private val time: Long, private val task: Runnable) : Delayed {

        private val start = System.currentTimeMillis()

        override fun compareTo(other: Delayed): Int {

            if (other is SunDelayTask) {
                return (this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS)).toInt()
            }

            return 0
        }

        override fun getDelay(unit: TimeUnit): Long {
            return unit.convert((start + time) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        fun doRun() {
            task.run()
        }
    }

}

object TestThreadPool {
    fun doTest() {
        println("Main >> TestThreadPool")

        val stuThreadPool = StuThreadPool(PoolTypeScheduled)

        val pool = stuThreadPool.threadPool

        if (pool is ScheduledExecutorService) {
            var count = 0

            if (false) {
                /**
                 * 按某种速率周期执行，3,6,9执行
                 * 即任务开始时，延时3s
                 */
                pool.scheduleAtFixedRate(Runnable {
                    println("scheduleAtFixedRate >> ${System.currentTimeMillis()} , count :${count++}")

                    Thread.sleep(1000)

                    if (count > 10) {
                        pool.shutdownNow()
                    }

                }, 1L, 3L, TimeUnit.SECONDS)

            } else {
                /**
                 * 在某个延迟后执行，4，8，12执行
                 * 即在任务完成后再延时3s
                 */
                pool.scheduleWithFixedDelay(Runnable {
                    println("scheduleWithFixedDelay >> ${System.currentTimeMillis()} , count :${count++}")

                    Thread.sleep(1000)
                    if (count > 10) {
                        pool.shutdownNow()
                    }
                }, 1L, 3L, TimeUnit.SECONDS)
            }


        } else {
            for (num in 1..30) {

                stuThreadPool.execute {
                    Thread.sleep(100)
                    println("Main >> ${System.currentTimeMillis()}  num :$num")
//                if (num == 5) {
//                    stuThreadPool.shutdown()
//                }
                }
            }

            println("Main >> ${System.currentTimeMillis()}  awaitTermination start")
            val result = stuThreadPool.awaitTermination(1000)
            println("Main >> ${System.currentTimeMillis()}  awaitTermination end , result :$result")

            if (!result) {
                stuThreadPool.shutdownNow()
            }
        }

    }
}

fun main() {
    println("Main >> StuThreadPool")

    TestThreadPool.doTest()
//    TestDelayQueue.doTest()

}