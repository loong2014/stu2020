package com.sunny.module.stu.A基础知识

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class B协程 {

    // 统一处理scope的异常，scope作用域中的异常没有被try-catch捕获，则抛到这里统一处理
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    // CoroutineContext 是一个关联数组，存储了一组上下文元素，用来配置协程的行为，这里通过 + 进行关联
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler)

    /**
     * 协程主要用于简化异步编程和非阻塞代码的编写。即使用同步的形式编写异步代码
     * 是一种轻量级的线程，可以在不阻塞当前线程的情况下执行长时间运行的任务
     */
    fun stu_coroutine() {
        // 定义一个协程任务
        val job = scope.launch {
            val result = withContext(Dispatchers.IO) {
                // 执行耗时任务，最后一行是返回结果
                // 判断当前协程是否被cancel
                if (!isActive) {
                }
                "abc"
            }
        }

        // 捕获 CancellationException 并作出处理
        job.invokeOnCompletion {

        }
        // 取消当前任务，会抛出一个 CancellationException
        job.cancel()
    }

    suspend fun stu_join() {
        val job = scope.launch(Dispatchers.IO) {
        }
        // 等待任务完成，join()函数会阻塞当前线程
        job.join()
    }

    suspend fun stu_async() {
        val job = scope.launch {
            // 建立多个任务
            val work1 = async {
                "work1"
            }
            val work2 = async {
                "work2"
            }
            val work3 = async {
                "work3"
            }

            // 等待任务完成，如果work还未完成，await()会挂起当前协程，直到work完成
            val result1 = work1.await()
            val result2 = work2.await()
            val result3 = work3.await()

            // 所有任务完成后，打印任务结果
            println("result :$result1, result2 :$result2, result3 :$result3")
        }
    }


    /**
     * 也可以使用 coroutineScope 函数来创建一个新的协程作用域，
     * 这样可以在一个协程中启动多个子协程，并等待它们全部完成
     *
     * 如果想在其中一个work异常的时候，能返回其它work的结果，则需要在async中进行try-catch处理
     */
    suspend fun doRequestsException(): List<String> = coroutineScope {
        // 建立多个任务
        val work1 = async {
            try {
                "work1"

            } catch (_: Exception) {
                "work1 error"
            }
        }
        val work2 = async {
            "work2"
        }
        val work3 = async {
            "work3"
        }

        listOf(work1.await(), work2.await(), work3.await())
    }

    /**
     * 异常处理  SupervisorJob 和 SupervisorScope
     * 在默认情况下，当一个协程的子协程抛出未捕获的异常时，这个异常会被传播给父协程和它的所有兄弟协程，
     * 导致整个协程层次结构被取消。
     * 如果你想要防止这种行为，可以使用 SupervisorJob 或 SupervisorScope。
     * 这两者都可以创建一个新的协程作用域，其中一个子协程的失败不会导致其它协程被取消。
     */
    fun sut_exception() {
        // SupervisorJob 和 SupervisorScope 的主要区别在于生命周期和作用范围。
        //  SupervisorJob 通常用于创建一个长期存在的协程作用域，比如一个 ViewModel 的生命周期，
        //  而 SupervisorScope 通常用于创建一个短期存在的协程作用域，比如一个网络请求的生命周期。

        // SupervisorJob 是一个特殊的 Job，它创建一个新的协程作用域，其中一个子协程的失败不会导致其它协程被取消。
        // 你可以将 SupervisorJob 作为一个上下文元素传递给协程构建器，例如：
        val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        scope.launch {
            // 如果这个协程抛出未捕获的异常，其它协程不会被取消
        }

        // supervisorScope 是一个函数，它创建一个临时的协程作用域，
        // 这个作用域中的一个子协程的失败不会导致其它协程被取消。
        // 你可以在协程中使用 supervisorScope 来启动一个新的协程作用域，例如：
        val job = CoroutineScope(Dispatchers.Main).launch {
            supervisorScope {
                launch {
                    // 如果这个协程抛出未捕获的异常，其它协程不会被取消
                }
            }
        }
    }

    /**
     * 协程和线程的区别
     * 协程（Coroutine）和线程（Thread）都是多任务处理中的概念，但它们的工作方式和使用场景有所不同。
     */
    fun CoroutineDifThread() {
        /*

        **线程：**

        线程是操作系统级别的并发单元，是程序执行流的最小单元。一个进程（应用程序）可以包含多个线程，这些线程共享进程的资源，如内存空间等。线程由操作系统调度，线程切换涉及到上下文切换，保存执行现场等，需要消耗一定的系统资源。

        **协程：**

        协程是一种用户态的轻量级线程，也称为微线程（Microthread），不直接由操作系统内核管理，而是由程序或者运行时（Runtime）控制。协程更轻量级，创建和切换的成本更低。协程的一个关键特性是可以挂起（suspend）和恢复（resume）。

        **协程与线程的对比：**

        1. **上下文切换：** 线程的上下文切换由操作系统控制，涉及到用户态和内核态的切换，需要保存和恢复大量状态，消耗的资源相对较大。而协程的上下文切换只发生在用户态，开销相对较小。

        2. **并发数量：** 线程的数量受到系统资源的限制，通常不能超过几千个。而协程非常轻量级，一个进程内可以创建数百万个协程。

        3. **堆栈：** 每个线程都有自己的堆栈空间，通常默认为1MB或更大。而协程的堆栈可以是动态的，可以在需要时增长和收缩，因此协程可以使用更少的内存。

        4. **阻塞：** 当线程执行到阻塞操作（如 I/O、sleep）时，操作系统会挂起该线程，切换到其他线程执行。而协程可以在执行到阻塞操作时，将控制权交回给协程调度器，让调度器切换到其他协程执行，从而实现非阻塞的异步操作。

        在 Kotlin 中，协程是一种非阻塞的异步编程模型，可以让你以同步的方式编写异步的代码。协程可以让你更容易地编写并发代码，并且避免了回调地狱的问题。
         */

    }

    /**
     * 协程调度器（Coroutine Dispatcher）决定了协程应该在哪个线程或线程池上执行。
     * 在 Kotlin 中，你可以通过在 `launch` 或 `async` 函数中指定调度器来控制协程的执行线程。
     */
    fun coroutine_dispatcher() {
        /*
        Kotlin 提供了几种内置的调度器：

        - `Dispatchers.Default`：用于 CPU 密集型任务，如计算和排序。
            这个调度器使用了一个共享的后台线程池。
            线程池的大小与 CPU 核心数相同。

        - `Dispatchers.IO`：用于 I/O 密集型任务，如读写文件、操作数据库和网络请求。
            这个调度器使用了一个共享的后台线程池。
            为了限制线程数，这个调度器对线程进行了限制，不会创建超过 CPU 核心数的线程。

        - `Dispatchers.Main`：用于更新 UI 和执行快速任务。在 Android 中，这个调度器使用了主线程。
            这个调度器只能在有主线程的环境中使用，如 Android 或 JVM 的 Swing 或 JavaFX。

        - `Dispatchers.Unconfined`：这是一个特殊的调度器，它在调用者的线程中立即执行协程，直到第一个挂起点。
            当协程恢复时，它在挂起函数所在的线程上继续执行，不受调度器的控制。
            这个调度器主要用于测试和调试。

        协程的调度过程由 Kotlin 的协程库控制。当你启动一个新的协程时，协程库会根据你指定的调度器选择一个线程来执行协程的任务。
        当协程执行到挂起函数时，协程库会挂起协程，并将控制权交回给调度器。
        当挂起函数完成时，协程库会根据调度器选择一个线程来恢复协程的执行。

        你也可以创建自己的调度器，例如，你可以使用 `newSingleThreadContext` 函数创建一个新的单线程调度器，或使用 `newFixedThreadPoolContext` 函数创建一个新的固定线程池调度器。
         */
        val singleThreadDispatcher = newSingleThreadContext("MyOwnThread")
        val fixedThreadPoolDispatcher = newFixedThreadPoolContext(4, "MyOwnThreadPool")
        scope.launch(singleThreadDispatcher) {

        }
        scope.launch(fixedThreadPoolDispatcher) {
        }

    }
}