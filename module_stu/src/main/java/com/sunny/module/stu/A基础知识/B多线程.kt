package com.sunny.module.stu.A基础知识

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


class B多线程 {

    /**
     * 线程是操作系统能够进行运算调度的最小单位。
     * 主要分为主线程和子线程，
     */
    fun 线程() {
        // 线程是操作系统能够进行运算调度的最小单位。

        /*
        1、主线程 又叫UI线程，负责处理与用户界面交互的任务，比如启动Activity/Service，处理BroadcastReceiver中onReceive()
        2、子线程 用于处理耗时任务，比如文件读写，网络请求，复杂的逻辑计算，数据库操作等
         */

    }

    /**
     * 线程池可以在需要时创建新线程，但在已有线程空闲时会重用这些线程，这样可以在多线程编程中提高性能。
     * 主要优点是减少了在创建和销毁线程上所花的时间以及系统资源的消耗。
     */
    fun stu_ThreadPoolExecutor() {

        // Java中的线程池主要由java.util.concurrent.ExecutorService接口表示，它有多个实现类，

        // 如ThreadPoolExecutor和ScheduledThreadPoolExecutor。

        val runnable = Runnable {
            // do something
        }

        val 计算密集型 = false
        val corePoolSize = if (计算密集型) {
            Runtime.getRuntime().availableProcessors()
        } else {
            Runtime.getRuntime().availableProcessors() * 2
        }

        // 如果线程池中正在执行的线程数超过corePoolSize，则将新线程放到queue中
        val workQueue = ArrayBlockingQueue<Runnable>(100)
        /*
        capacity的大小决定了可以接受多少个等待执行的任务
        1、任务的数量：
            如果你的应用会产生大量的短期任务，需要一个较大的队列来存储这些任务。
            如果任务的数量相对较少，那么队列可以设定得较小。
        2、任务的执行时间：
            如果任务的执行时间较长，那么队列可能会很快就被填满，无法及时处理队列中的新任务，可能需要一个较大的队列。
            如果任务的执行时间较短，那么队列可以设定得较小。
        3、系统资源：队列越大，占用的内存越多。
            如果你的应用运行在内存有限的设备上，那么可能需要设定一个较小的队列。

        如果workQueue的大小设定得过大，可能会导致大量的任务堆积在队列中，导致延迟增大；
            如果设定得过小，可能会导致过多的任务被拒绝。
            因此，设定workQueue的大小需要根据你的应用的具体需求和设备的硬件性能来决定。
         */
        // 常用工作队列
        // 这些阻塞队列的主要区别在于数据结构（数组或链表）、是否有界、是否支持优先级排序、是否支持延迟、是否支持双向操作等。
        // 在选择阻塞队列时，应根据应用的需求来选择合适的阻塞队列。

        // 一个由数组结构组成的有界阻塞队列。这个队列按 FIFO（先进先出）原则对元素进行排序。
        val arrayBlockingQueue = ArrayBlockingQueue<Runnable>(10)
        arrayBlockingQueue.add(runnable)

        // 一个由链表结构组成的可选定长阻塞队列。这个队列按 FIFO（先进先出）排序元素。此队列的默认和最大长度为Integer.MAX_VALUE。
        val linkedBlockingQueue = LinkedBlockingQueue<Runnable>(10)
        linkedBlockingQueue.add(runnable)

        // 一个支持优先级排序的无界阻塞队列。优先级由元素的自然顺序或者提供的Comparator决定。
        val priorityBlockingQueue = PriorityBlockingQueue<Runnable>(10)
        priorityBlockingQueue.put(runnable)

        // 一个使用优先级队列实现的无界阻塞队列。只有在延迟期满时才能从中提取元素。
        // 这种队列的头部是延迟期满后保存时间最长的元素。如果延迟都还没有期满，则队列没有头部，并且poll将返回null。
        class DelayedElement(private val delayTime: Long, private val task: Runnable) : Delayed {
            // 到期时间
            private val expire: Long = System.currentTimeMillis() + delayTime

            override fun getDelay(unit: TimeUnit): Long {
                return unit.convert(expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            }

            override fun compareTo(other: Delayed): Int {
                return (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS)).toInt()
            }

            override fun toString(): String {
                return task.toString()
            }
        }

        val delayQueue = DelayQueue<DelayedElement>()
        delayQueue.put(DelayedElement(1_000, runnable))

        // 一个不存储元素的阻塞队列。每一个put操作必须等待一个take操作，否则不能继续添加元素，反之亦然。
        // fair如果为true，则等待线程按照FIFO顺序进行争用；否则未指定顺序。
        val synchronousQueue = SynchronousQueue<Runnable>(true)
        synchronousQueue.add(runnable)

        // 一个由链表结构组成的无界阻塞队列，它是TransferQueue接口的一个实现。
        // LinkedTransferQueue的一个显著特性是它多了一个transfer()方法，
        // 此方法如果没有消费者正在等待接收元素（即队列中没有消费者正在等待），那么调用transfer()的线程会阻塞，直到元素被消费者接收为止。
        val linkedTransferQueue = LinkedTransferQueue<Runnable>()
        linkedTransferQueue.transfer(runnable) // 添加会阻塞

        // 一个由链表结构组成的双向阻塞队列。
        // 队列头部的元素是最近由 addFirst 或 offerFirst 插入的元素。
        // 队列尾部的元素是最近由 addLast 或 offerLast 插入的元素。
        val linkedBlockingDeque = LinkedBlockingDeque<Runnable>()
        linkedBlockingDeque.add(runnable)


        // 如果queue满了，可以开启的 新线程个数 来执行任务。
        val maximumPoolSize = corePoolSize

        // 是线程池中空闲线程的存活时间，即超过这个时间后，线程池中的空闲线程（超过corePoolSize的部分）将被终止。
        val keepAliveTime = 5_000L
        /*
        1、任务的频率：
            如果你的应用会频繁地产生新任务，那么可以设定较长的keepAliveTime，这样可以避免频繁地创建和销毁线程，从而提高性能。
            如果新任务产生的频率较低，那么keepAliveTime可以设定得较短，这样可以更快地释放不需要的线程，节省系统资源。
        2、系统资源：
            如果你的应用运行在资源有限的设备上，那么可能需要设定较短的keepAliveTime，这样可以更快地释放不需要的线程，节省系统资源。
            如果系统资源充足，那么可以设定较长的keepAliveTime，这样可以减少创建和销毁线程的开销，提高性能。
        3、应用的性能需求：
            如果你的应用对性能有较高的需求，那么可能需要设定较长的keepAliveTime，这样可以减少创建和销毁线程的开销，提高性能。
            如果你的应用对性能的需求较低，那么keepAliveTime可以设定得较短，这样可以更快地释放不需要的线程，节省系统资源。

        如果keepAliveTime设定得过长，可能会导致不需要的线程长时间占用系统资源；
            如果设定得过短，可能会导致频繁地创建和销毁线程，增加系统开销。
            因此，设定keepAliveTime需要根据你的应用的具体需求和设备的硬件性能来决定。
         */

        // var threadFactory = Executors.defaultThreadFactory()
        val threadFactory = object : ThreadFactory {
            private val threadNumber = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                // 自定义线程名称
                return Thread(r, "myThreadPool-thread-" + threadNumber.getAndIncrement())
            }
        }

        // RejectedExecutionHandler是一个接口，它定义了当任务被拒绝时应该如何处理：
        val handler: RejectedExecutionHandler = ThreadPoolExecutor.CallerRunsPolicy()
        /*
        1、ThreadPoolExecutor.CallerRunsPolicy（直接执行被拒绝的任务）
        2、ThreadPoolExecutor.AbortPolicy（默认策略，直接抛出异常）
        3、ThreadPoolExecutor.DiscardPolicy（直接丢弃任务，不抛出异常）
        4、ThreadPoolExecutor.DiscardOldestPolicy（丢弃队列最前面的任务，然后重新尝试执行任务）。
         */

        val executor = ThreadPoolExecutor(
            corePoolSize,// corePoolSize：线程池的基本大小
            maximumPoolSize, // maximumPoolSize：线程池最大大小
            keepAliveTime, // keepAliveTime：闲置线程等待新任务的最长时间
            TimeUnit.MILLISECONDS, // unit：keepAliveTime的时间单位
            workQueue, // workQueue：任务队列，被提交但尚未被执行的任务
            threadFactory, // threadFactory：生成新线程的工厂
            handler // handler：当任务不能在execute方法中被执行时（例如，因为它已关闭或因为饱和），我们如何处理这个任务的策略
        )
        executor.execute(runnable)

        // 创建一个固定大小的线程池，可以一次性预热创建所有核心线程，即创建的线程池容量固定，可以控制线程最大并发数。
        val newFixedThreadPool = Executors.newFixedThreadPool(corePoolSize)
        newFixedThreadPool.execute(runnable)

        // 创建一个只有一个线程的线程池。这个线程池的核心线程数量和最大线程数量都是1，
        // 工作队列是LinkedBlockingQueue，容量为Integer.MAX_VALUE。这意味着，所有的任务都会在一个线程中顺序执行。
        val newSingleThreadExecutor = Executors.newSingleThreadExecutor(threadFactory)
        newSingleThreadExecutor.execute(runnable)

        // 创建一个可缓存的线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        val newCachedThreadPool = Executors.newCachedThreadPool(threadFactory)
        newCachedThreadPool.execute(runnable)

        // 创建一个使用工作窃取算法的线程池。这个线程池的核心线程数量和最大线程数量都是parallelism，
        // 工作队列是一个无界的LinkedTransferQueue。
        // 工作窃取算法可以充分利用多核CPU的性能，当一个线程的任务执行完成后，它可以从其他线程的队列中窃取任务来执行。
        val newWorkStealingPool = Executors.newWorkStealingPool(corePoolSize)
        newWorkStealingPool.execute(runnable)

        // 创建一个定长线程池，支持定时及周期性任务执行
        val newScheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize)
        newScheduledThreadPool.schedule(runnable, 10L, TimeUnit.SECONDS)

        // 使用场景
        /*
        1、计算密集型任务：这种任务需要大量的计算，没有I/O。
            例如运行一个复杂的算法。这种情况下，CPU是唯一的限制因素，我们需要尽可能的利用CPU。
            在这种情况下，最理想的线程数等于CPU的核心数。
        2、I/O密集型任务：这种任务需要大量的I/O。
            例如文件操作，网络操作，数据库操作等等。
            这种情况下，线程并不是一直在执行任务，因此可以适当的增加线程数，比如2*CPU的核心数。
        3、混合型任务：如果是一些即涉及I/O又涉及计算的任务，需要根据实际情况来调整线程池大小。
            如果多数为I/O密集型，参考I/O密集型的线程数设置，
            如果多数为计算密集型，那就参考计算密集型的线程数设置。
         */

    }

    /**
     * 用于处理一些不需要用户交互并且希望在后台完成的任务
     * 使用场景包括：
     *  1、长时间运行的任务：例如下载文件，上传文件等。
     *  2、不需要用户交互的任务：例如定时检查更新，后台数据同步等。
     */
    fun stu_IntentService(context: Context) {
        // 特性
        /*
        1、创建一个默认的工作线程，用于执行传递给 onStartCommand() 的所有 Intent 请求。
        2、创建的工作线程是单线程的，所以它只会同时处理一个 Intent。如果你发送了多个 Intent，那么它们将会排队，每次只处理一个。
        3、当所有请求处理完成后，IntentService 会自动停止，所以你不需要关心何时去停止服务。
        */
        class TaskIntentService : IntentService("TaskIntentService") {
            override fun onHandleIntent(intent: Intent?) {
                // 在这里处理你的任务
            }

            override fun onDestroy() {
                super.onDestroy()
                // 当所有请求处理完成后，IntentService 会被停止
            }
        }

        val intent = Intent(context, TaskIntentService::class.java)
        // 可以通过 Intent 传递数据
        intent.putExtra("extra_data", "some data")
        context.startService(intent)

        // IntentService 与 Service 的区别
        /*
        1、生命周期管理：
            Service 在启动后会一直运行，除非系统资源紧张或者你手动停止。
            IntentService 在完成所有任务后会自动停止，你不需要手动管理其生命周期。
            都可以通过stopService来停止
        2、工作线程：
            Service 默认在主线程中运行，如果你在 Service 中处理一些耗时的任务，如网络请求或者数据库操作，你需要手动创建新的线程，否则可能会导致应用卡顿或者崩溃。
            IntentService 会自动创建一个工作线程来处理所有的 Intent 请求，你不需要关心线程管理。
        3、任务处理：
            Service 可以同时处理多个请求。
            IntentService 是单线程的，它会把所有的 Intent 请求放入队列，然后依次处理。
        4、绑定：
            你可以绑定一个 Service 来与其进行交互，
            但你不能绑定一个 IntentService。
         */
        // startId
        /*
        1、startId 是由系统定义的。当你调用 startService(Intent) 方法来启动一个服务时，系统会调用该服务的 onStartCommand(Intent, int, int) 方法，
        并为每个 Intent 提供一个唯一的 startId。这个 startId 用于区分不同的启动请求。
        2、在 IntentService 中，系统在每次调用 onStartCommand(Intent, int, int) 方法时，都会将新的 Intent 和对应的 startId 添加到工作队列中。
        然后，IntentService 的工作线程会依次处理这些 Intent。
        3、当 IntentService 处理完一个 Intent 时，它会调用 stopSelf(int startId) 来请求停止服务。
        但是，服务只会在所有的 startId 都被处理过（也就是所有的 Intent 都被处理完毕）后才会停止。

        这就是 startId 的来源和作用。它由系统提供，用于区分和追踪不同的服务启动请求。
         */
    }


    /**
     * 允许我们在后台线程上执行长时间运行的任务，然后在UI线程上发布结果。
     * 从Android 11(api 30)开始，AsyncTask已被弃用
     */
    fun stu_AsyncTask() {
        class MyAsyncTask : AsyncTask<String, Int, String>() {
            override fun onPreExecute() {
                // 在这里，你可以在任务开始前做一些准备工作，例如显示一个进度条。
            }

            override fun doInBackground(vararg params: String): String {
                // 在这里执行耗时任务，例如从网络下载数据。
                // 这个方法在后台线程上运行，所以你不能在这里更新UI。
                // 这个方法的返回值将被传递给onPostExecute()。
                publishProgress(10)
                return "Result"
            }

            override fun onProgressUpdate(vararg values: Int?) {
                // 在这里更新任务的进度。这个方法在UI线程上运行，所以你可以在这里更新UI。
            }

            override fun onPostExecute(result: String) {
                // 在这里处理任务的结果。这个方法在UI线程上运行，所以你可以在这里更新UI。
                // 参数result是doInBackground()的返回值。
            }
        }

        val asyncTask = MyAsyncTask()
        asyncTask.execute("get information")
    }

}