package com.sunny.lib.common.thread

import android.os.Looper


class StuThreadLocal {

    /**
     * https://www.jianshu.com/p/0741d802aaa2
     *
     * https://www.cnblogs.com/ice-image/p/14547180.html
     *
     * ThreadLocal?
     *
     * ThreadLocal和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。
     * ThreadLocal本身并不存储值，它只是作为一个key来让线程从ThreadLocalMap获取value。
     *
     * // 1
     * 在同步机制中，通过对象的锁机制保证同一时间只有一个线程访问变量。
     * 这时该变量是多个线程共享的，使用同步机制要求程序慎密地分析什么时候对变量进行读写，
     * 什么时候需要锁定某个对象，什么时候释放对象锁等繁杂的问题，程序设计和编写难度相对较大。
     *
     * // 2
     * 而ThreadLocal则从另一个角度来解决多线程的并发访问。
     * ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。
     * 因为每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。
     * ThreadLocal提供了线程安全的共享对象，在编写多线程代码时，可以把不安全的变量封装进ThreadLocal。
     *
     * // 3
     * 总的来说，对于多线程资源共享的问题，同步机制采用了“以时间换空间”的方式，
     * 而ThreadLocal采用了“以空间换时间”的方式。
     * 前者仅提供一份变量，让不同的线程排队访问，
     * 而后者为每一个线程都提供了一份变量，因此可以同时访问而互不影响。
     */

    /**
     * ThreadLocalMap？
     *
     * 在Thread中定义：ThreadLocal.ThreadLocalMap threadLocals
     * 通过[ThreadLocal.set]和[ThreadLocal.get]进行数据的存储/获取，
     *
     * ThreadLocalMap 是 ThreadLocal 的静态内部类，
     * 当一个线程有多个 ThreadLocal 时，需要一个容器来管理多个 ThreadLocal，ThreadLocalMap 的作用就是管理线程中多个 ThreadLocal。
     *
     * ThreadLocalMap 其实就是一个简单的 Map 结构，底层是数组，有初始化大小，也有扩容阈值大小，数组的元素是 Entry。
     * ThreadLocalMap的数据结构是一个用数组表示的环，数组长度必须是2的次幂，
     * 同样通过hash方式确定节点在数组中的下标（hash值是ThreadLocal的递增变量，而不是hashcode值），
     * 对于hash冲突的情况，采用线性探测法，直接将元素防止对应下标后面的下一个空闲单元。
     */

    /**
     * Entry？
     *
     * Entry是ThreadLocalMap的内部类，用来表示其中的节点，继承了弱引用WeakReference<ThreadLocal<?>>。
     *
     * Entry 的 key 就是 ThreadLocal 的引用，value 是 ThreadLocal 的值。
     * 同时，Entry也继承WeakReference，所以说Entry所对应key（ThreadLocal实例）的引用是一个弱引用。
     */

    /**
     * 擦除机制？
     * ThreadLocalMap中内部类Entry，继承了WeakReference，其key值是弱引用类型，
     * 在没有强引用时会被gc回收，因此ThreadLocalMap要及时对这部分过期节点进行擦除。
     *
     * 1、根据数组下标，擦除某个节点[ThreadLocalMap.expungeStaleEntry] (staleSlot)
     * 擦除staleSlot处的无效节点，同时扫描处于staleSlot + 1 到 下一个null节点之间的节点，对于过期节点进行擦除，有效节点rehash，判断是否需要修改位置。
     *
     * 2、全量扫描擦除[ThreadLocalMap.expungeStaleEntries] ()
     * 全量扫描擦除，遍历数组中的所有节点，对于过期节点调用擦除方法expungeStaleEntry进行擦除。
     *
     * 3、启发式扫描擦除[ThreadLocalMap.cleanSomeSlots] (i,n)
     * 从 i+1 开始扫描检查，如果连续 n个单元不需要擦除则结束方法，
     * 否则找到一个过期节点，重置计数，将n置为数组长度，重新开始新一轮的扫描。只有扫描过程中有一个过期节点，则认为擦除成功，返回true。
     *
     * 4、根据key值移除节点[ThreadLocalMap.remove] (ThreadLocal<?>)
     * 找到节点后不是简单的将该节点置为null，还需要调用expungeStaleEntry擦除方法，不然该节点后面的hash冲突节点会无法通过getEntry获取到。
     *
     */

    /**
     * Thread、ThreadLocal 以及 ThreadLocalMap 关系？
     *
     * Thread中有一个类型为 ThreadLocalMap 的成员变量
     * ThreadLocalMap的数据结构是一个用数组表示的环。存储的数据结构是一个键值对构成的Entry
     * Entry的key是一个ThreadLocal类型的引用，value是ThreadLocal的值
     *
     *
     * ThreadLocal 是如何实现线程隔离的呢？
     * ThreadLocal之所以能达到变量的线程隔离，就是因为每个线程都有一个自己的ThreadLocalMap对象。
     * 在ThreadLocalMap中通过对同一个ThreadLocal实例进行数据的存储/读取，从而达到线程隔离的目的。
     *
     *
     * ThreadLocal 内存泄漏问题？
     * ThreadLocal 在没有外部强引用时，发生 GC时会被回收，那么 ThreadLocalMap 中保存的 key 值就变成了 null，
     * 而 Entry 又被 threadLocalMap 对象引用，threadLocalMap 对象又被 Thread 对象所引用，
     * 那么当 Thread 一直不终结的话，value 对象就会一直存在于内存中，也就导致了内存泄漏，直至 Thread 被销毁后，才会被回收。
     *
     * ThreadLocal内存泄漏的根源是：由于ThreadLocalMap的生命周期跟Thread一样长，如果没有手动删除对应key就会导致内存泄漏，而不是因为弱引用。
     *
     * 在使用完 ThreadLocal 变量后，需要我们手动 remove 掉，防止 ThreadLocalMap 中的 Entry 一直保持对 value 的强引用，导致 value 不能被回收。
     *
     */
    fun stu2() {
        val threadLocal = ThreadLocal<Any>()

        threadLocal.set("aaa")

    }

    fun stu() {


        // 使用ThreadLocal进行数据存储
        val thread = ThreadLocal<Looper>()
        thread.set(Looper.getMainLooper())
        val looper = thread.get()

        //
        Thread(Runnable {
            // 在线程中初始化looper
            Looper.prepare()

            // 开始loop
            Looper.loop()
        }).start()
    }
}