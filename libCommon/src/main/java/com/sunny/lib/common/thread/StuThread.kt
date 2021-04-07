package com.sunny.lib.common.thread

import com.sunny.lib.common.R


class StuThread {

    /**
     * 什么是进程，线程？
     *
     * 进程：每个进程都有独立的代码和数据空间（进程上下文），进程间的切换会有较大的开销，一个进程包含1--n个线程。（进程是资源分配的最小单位）
     * 线程：同一类线程共享代码和数据空间，每个线程有独立的运行栈和程序计数器(PC)，线程切换开销小。（线程是cpu调度的最小单位）
     *
     * 线程和进程一样分为五个阶段：创建、就绪、运行、阻塞、终止。
     * 多进程是指操作系统能同时运行多个任务（程序）。
     * 多线程是指在同一程序中有多个顺序流在执行。
     *
     *
     * https://blog.csdn.net/evankaka/article/details/44153709
     */

    /**
     * 如何实现多线程？
     *
     * 1. 继承Thread类
     * 2. 实现Runnable接口
     */

    /**
     * 线程生命周？
     *
     * 1、新建状态（New）：新创建了一个线程对象。
     * 2、就绪状态（Runnable）：线程对象创建后，其他线程调用了该对象的start()方法。该状态的线程位于可运行线程池中，变得可运行，等待获取CPU的使用权。
     * 3、运行状态（Running）：就绪状态的线程获取了CPU，执行程序代码。
     * 4、阻塞状态（Blocked）：阻塞状态是线程因为某种原因放弃CPU使用权，暂时停止运行。直到线程进入就绪状态，才有机会转到运行状态。
     * 5、死亡状态（Dead）：线程执行完了或者因异常退出了run()方法，该线程结束生命周期。
     */

    /**
     * 线程类的一些常用方法?
     * sleep(): 强迫一个线程睡眠Ｎ毫秒。
     * isAlive(): 判断一个线程是否存活。 
     * join(): 等待线程终止。 
     * activeCount(): 程序中活跃的线程数。 
     * enumerate(): 枚举程序中的线程。 
     * currentThread(): 得到当前线程。 
     * isDaemon(): 一个线程是否为守护线程。 
     * setDaemon(): 设置一个线程为守护线程。(用户线程和守护线程的区别在于，是否等待主线程依赖于主线程结束而结束) 
     * setName(): 为线程设置一个名称。 
     * wait(): 强迫一个线程等待。
     * notify(): 通知一个线程继续运行。 
     * setPriority(): 设置一个线程的优先级。
     */

    /**
     * 线程优先级？
     *
     * 在一个运行系统中，如果较高优先级的线程没有调用 sleep 方法，又没有受到 I\O 阻塞，
     * 那么，较低优先级线程只能等待所有较高优先级的线程运行结束，才有机会运行。
     *
     *
     * Java线程的优先级用整数表示，取值范围是1~10，Thread类有以下三个静态常量：
     *
     * static int MIN_PRIORITY：线程可以具有的最低优先级，取值为1。
     * static int NORM_PRIORITY：分配给线程的默认优先级，取值为5。
     * static int MAX_PRIORITY：线程可以具有的最高优先级，取值为10。
     *
     * 通过Thread类的setPriority()和getPriority()方法分别用来设置和获取线程的优先级。
     *
     * 线程的优先级有继承关系，比如A线程中创建了B线程，那么B将和A具有相同的优先级。
     *
     * JVM提供了10个线程优先级，但与常见的操作系统都不能很好的映射。如果希望程序能移植到各个操作系统中，
     * 应该仅仅使用Thread类中的三个静态常量作为优先级，这样能保证同样的优先级采用了同样的调度方式。
     */

    /**
     * 线程调度？
     *
     * 1、调整线程优先级：Java线程有优先级，优先级高的线程会获得较多的运行机会。
     * 通过Thread类的setPriority()和getPriority()方法分别用来设置和获取线程的优先级。
     *
     * 2、线程睡眠：Thread.sleep(long millis)方法，转入【阻塞状态】。millis参数设定睡眠的时间，以毫秒为单位。
     * 当睡眠结束后，就转为就绪（Runnable）状态。sleep()平台移植性好。
     *
     * 3、线程等待：Object类中的wait()方法，转入【阻塞状态】。直到其他线程调用此对象的 notify() 方法或 notifyAll() 唤醒方法。
     * 这个两个唤醒方法也是Object类中的方法。
     *
     * 4、线程让步：Thread.yield()方法，转入【就绪状态】。暂停当前正在执行的线程对象，把执行机会让给相同或者更高优先级的线程。
     *
     * 5、线程加入：join()方法，转入【阻塞状态】，等待其他线程终止。在当前线程中调用另一个线程的join()方法，
     * 则当前线程转入阻塞状态，直到另一个进程运行结束，当前线程再由阻塞转为就绪状态。
     *
     * 6、线程唤醒：Object类中的notify()方法，唤醒在此对象监视器上等待的单个线程。
     * 如果所有线程都在此对象上等待，则会选择唤醒其中一个线程。选择是任意性的，并在对实现做出决定时发生。
     * 线程通过调用其中一个 wait 方法，在对象的监视器上等待。
     * 直到当前的线程放弃此对象上的锁定，才能继续执行被唤醒的线程。
     * 被唤醒的线程将以常规方式与在该对象上主动同步的其他所有线程进行竞争；
     * 例如，唤醒的线程在作为锁定此对象的下一个线程方面没有可靠的特权或劣势。
     *
     * 类似的方法还有一个notifyAll()，唤醒在此对象监视器上等待的所有线程。
     *
     * 7、线程中断：interrupt()，不要以为它是中断某个线程！它只是给线程发送一个中断信号，
     * 通过调用线程的interrupt方法，打断线程的暂停状态（如果线程正在执行正常的代码，则不会被打断，只有进入阻塞后才好抛出InterruptedException）
     * 通过抛出异常，来结束线程，但如果你吃掉了这个异常，那么这个线程还是不会中断的！
     */

    /**
     * sleep和wait
     *
     * 相同：
     * 1、阻塞当前线程，并释放cpu控制权
     * 2、都可以制定阻塞的时长
     * 3、都可以通过interrupt()方法打断阻塞状态，从而使线程立刻抛出InterruptedException异常

     *
     * 区别：
     * 1、sleep是线程中的方法；但是wait是Object中的方法。
     * 2、锁处理机制不同：每个对象都有一个锁来控制同步访问。Synchronized关键字可以和对象的锁交互，来实现线程的同步。
     * sleep方法没有释放锁，而wait方法释放了锁，使得其他线程可以使用同步控制块或者方法。
     *
     * 3、使用区域不同：sleep()可以用在任何地方调用；wait()方法必须放在同步代码块或者同步方法中使用，
     * 4、唤醒不同：sleep不需要被唤醒（休眠之后退出阻塞）；但是wait需要（不指定时间时，需要调用此对象的notify()或者notifyAll()方法去唤醒）。
     */

    /**
     * https://blog.csdn.net/u014561933/article/details/58639411
     * 在Java中，每个对象都有两个池，锁(monitor)池和等待池
     *
     * 锁池：想要拥有该对象的锁的拥有权的线程
     *
     * 假设线程A已经拥有了某个对象(注意:不是类)的锁，而其它的线程想要调用这个对象的某个synchronized方法(或者synchronized块)，
     * 由于这些线程在进入对象的synchronized方法之前必须先获得该对象的锁的拥有权，但是该对象的锁目前正被线程A拥有，
     * 所以这些线程就进入了该对象的锁池中。
     *
     *
     * 等待池：调用对象wait()方法的线程
     * 假设一个线程A调用了某个对象的wait()方法，线程A就会释放该对象的锁(因为wait()方法必须出现在synchronized中，
     * 这样自然在执行wait()方法之前线程A就已经拥有了该对象的锁)，同时线程A就进入到了该对象的等待池中。
     * 如果另外的一个线程调用了相同对象的notifyAll()方法，那么处于该对象的等待池中的线程就会全部进入该对象的锁池中，准备争夺锁的拥有权。
     * 如果另外的一个线程调用了相同对象的notify()方法，那么仅仅有一个处于该对象的等待池中的线程(随机)会进入该对象的锁池.
     *
     * 优先级高的线程竞争到对象锁的概率大，假若某线程没有竞争到该对象锁，它还会留在锁池中，
     * 唯有线程再次调用 wait()方法，它才会重新回到等待池中。
     * 而竞争到对象锁的线程则继续往下执行，直到执行完了 synchronized 代码块，它会释放掉该对象锁，这时锁池中的线程会继续竞争该对象锁。
     *
     *
     * （一）、等待阻塞：运行的线程执行wait()方法，JVM会把该线程放入等待池中。(wait会释放持有的锁)
     * （二）、同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线程放入锁池中。
     * （三）、其他阻塞：运行的线程执行sleep()或join()方法，或者发出了I/O请求时，JVM会把该线程置为阻塞状态。
     * 当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态。（注意,sleep是不会释放持有的锁）
     */

    /**
     * Synchronized？
     * synchronized是Java中的关键字，是一种同步锁。作用主要有三个：
     * （1）确保线程互斥的访问同步代码
     * （2）保证共享变量的修改能够及时可见
     * （3）有效解决重排序问题。
     *
     * 它修饰的对象有以下几种：
     *
     * 1、修饰一个方法：synchronized method(){}：可以防止多个线程同时访问【这个对象】的 synchronized 方法。
     * （如果一个对象有多个synchronized方法，只要一个线程访问了其中的一个synchronized方法，其它线程不能同时访问这个对象中任何一个synchronized方法）。
     * 但是，不同的对象实例的synchronized方法是不相干扰的。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法；
     *
     * 2、修饰一个静态的方法：synchronized static staticMethod{}：防止多个线程同时访问【这个类】中的synchronized static 方法。
     *
     * 3、修饰一个代码块：synchronized(对象){}：被修饰的代码块称为同步语句块，表示只对这个区块的资源实行互斥访问。
     * 如果()中的对象==this，则【这个对象】的synchronized方法不能被同时访问。
     * 如果()中的对象!=this，则只对【这个区块】的资源进行互斥访问，以及对象里的synchronized方法
     */

    /**
     * Synchronized升级，在jdk1.6~1.7的版本中进行了升级
     *
     * 我们知道，java’线程其实是映射在内核之上的，线程的挂起和恢复会极大的影响开销。
     *
     *
     * 1、线程自旋和适应性自旋
     * 自旋：当有个线程A去请求某个锁的时候，这个锁正在被其它线程占用，但是线程A并不会马上进入阻塞状态，而是循环请求锁(自旋)。
     * 这样做的目的是因为很多时候持有锁的线程会很快释放锁的，线程A可以尝试一直请求锁，没必要被挂起放弃CPU时间片，
     * 因为线程被挂起然后到唤醒这个过程开销很大,当然如果线程A自旋指定的时间还没有获得锁，仍然会被挂起。
     *
     * 自适应性自旋：自适应性自旋是自旋的升级、优化，自旋的时间不再固定，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态决定。
     * 例如线程如果自旋成功了，那么下次自旋的次数会增多，因为JVM认为既然上次成功了，那么这次自旋也很有可能成功，那么它会允许自旋的次数更多。
     * 也就是是赋予了自旋一种学习能力，它并不固定自旋几次。他可以根据它前面线程的自旋情况，从而调整它的自旋，甚至是不经过自旋而直接挂起。
     *
     *
     * 2、锁消除
     * 锁消除：就是把不必要的同步在编译阶段进行移除。对一些代码上要求同步，但是被检测到不可能存在共享数据竞争的锁进行消除。
     * 这里所说的锁消除并不一定指代是你写的代码的锁消除，我打一个比方：
     * 在jdk1.5以前，我们的String字符串拼接操作其实底层是StringBuffer来实现的，而在jdk1.5之后，那么是用StringBuilder来拼接的。
     * 我们知道，StringBuffer是一个线程安全的类，也就是说每个append方法都会同步，
     * 通过指针逃逸分析（就是变量不会外泄），我们发现在这段代码并不存在线程安全问题，这个时候就会把这个同步锁消除。
     *
     * StringBuffer sb = new StringBuffer();
     * sb.append("qwe");
     * sb.append("asd");
     *
     * 3、锁粗化
     * 在用synchronized的时候，我们都讲究为了避免大开销，尽量同步代码块要小。那么为什么还要加粗呢？
     * 我们继续以上面的字符串拼接为例，我们知道在这一段代码中，每一个append都需要同步一次，
     * 那么我可以把锁粗化到第一个append和最后一个append（这里不要去纠结前面的锁消除，我只是打个比方）
     *
     */

    /**
     * https://www.jianshu.com/p/36eedeb3f912
     *
     * 锁状态
     * 锁主要存在四种状态，依次是：无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态，他们会随着竞争的激烈而逐渐升级。
     * 注意锁可以升级不可降级，这种策略是为了提高获得锁和释放锁的效率。
     *
     *
     * 重量级锁：
     * 内置锁在Java中被抽象为监视器锁（monitor）。在JDK 1.6之前，监视器锁可以认为直接对应底层操作系统中的互斥量（mutex）。
     * 这种同步方式的成本非常高，包括系统调用引起的内核态与用户态切换、线程阻塞造成的线程切换等。因此，后来称这种锁为“重量级锁”。
     *
     *
     * 轻量级锁：
     * 轻量级锁的目标是，减少无实际竞争情况下，使用重量级锁产生的性能消耗，包括系统调用引起的内核态与用户态切换、线程阻塞造成的线程切换等。
     * 顾名思义，轻量级锁是相对于重量级锁而言的。使用轻量级锁时，不需要申请互斥量，
     * 仅仅将Mark Word中的部分字节CAS更新指向线程栈中的Lock Record，如果更新成功，则轻量级锁获取成功，记录锁状态为轻量级锁；
     * 否则，说明已经有线程获得了轻量级锁，目前发生了锁竞争（不适合继续使用轻量级锁），接下来膨胀为重量级锁。
     *
     * 偏向锁：
     * 是消除数据在无竞争情况下的同步原语，进一步提高程序的运行性能。
     * 如果说轻量级锁是在无竞争的情况下使用CAS操作去消除同步使用的互斥量，那么偏向锁就是在无竞争的情况下把整个同步都消除掉，连CAS操作都不用做了。
     * 偏向锁默认是开启的，也可以关闭。
     *
     * 在没有实际竞争的情况下，还能够针对部分场景继续优化。如果不仅仅没有实际竞争，自始至终，使用锁的线程都只有一个，那么，维护轻量级锁都是浪费的。
     * 偏向锁的目标是，减少无竞争且只有一个线程使用锁的情况下，使用轻量级锁产生的性能消耗。
     * 轻量级锁每次申请、释放锁都至少需要一次CAS，但偏向锁只有初始化时需要一次CAS。
     *
     * “偏向”的意思是，偏向锁假定将来只有第一个申请锁的线程会使用锁（不会有任何线程再来申请锁），因此，
     * 只需要在Mark Word中CAS记录owner（本质上也是更新，但初始值为空），如果记录成功，则偏向锁获取成功，
     * 记录锁状态为偏向锁，以后当前线程等于owner就可以零成本的直接获得锁；否则，说明有其他线程竞争，膨胀为轻量级锁。
     *
     * 偏向锁无法使用自旋锁优化，因为一旦有其他线程申请锁，就破坏了偏向锁的假定。
     *
     *
     * 特点：
     * 偏向锁：无实际竞争，且将来只有第一个申请锁的线程会使用锁。
     * 轻量级锁：无实际竞争，多个线程交替使用锁；允许短时间的锁竞争。
     * 重量级锁：有实际竞争，且锁竞争时间长。
     * 另外，如果锁竞争时间短，可以使用自旋锁进一步优化轻量级锁、重量级锁的性能，减少线程切换。
     * 另外，如果锁竞争时间短，可以使用自旋锁进一步优化轻量级锁、重量级锁的性能，减少线程切换。
     *
     *
     * 锁的分配和膨胀：图[getLockMap]
     *
     *
     *
     * 公平锁：
     * 多个线程按照申请锁的顺序去获得锁，线程会直接进入队列去排队，永远都是队列的第一位才能得到锁。
     * 优点：所有的线程都能得到资源，不会饿死在队列中。
     * 缺点：吞吐量会下降很多，队列里面除了第一个线程，其他的线程都会阻塞，cpu唤醒阻塞线程的开销会很大。
     *
     * 非公平锁：
     * 多个线程去获取锁的时候，会直接去尝试获取，获取不到，再去进入等待队列，如果能获取到，就直接获取到锁。
     * 优点：可以减少CPU唤醒线程的开销，整体的吞吐效率会高点，CPU也不必取唤醒所有线程，会减少唤起线程的数量。
     * 缺点：你们可能也发现了，这样可能导致队列中间的线程一直获取不到锁或者长时间获取不到锁，导致饿死。
     *
     *
     * 自旋锁：
     * 使用-XX:-UseSpinning参数关闭自旋锁优化；-XX:PreBlockSpin参数修改默认的自旋次数。
     *
     * 自旋锁缺点：
     * 1、单核处理器上，不存在实际的并行，当前线程不阻塞自己的话，旧owner就不能执行，锁永远不会释放，此时不管自旋多久都是浪费；
     *
     * 进而，如果线程多而处理器少，自旋也会造成不少无谓的浪费。
     * 2、自旋锁要占用CPU，如果是计算密集型任务，这一优化通常得不偿失，减少锁的使用是更好的选择。
     *
     * 3、如果锁竞争的时间比较长，那么自旋通常不能获得锁，白白浪费了自旋占用的CPU时间。
     * 这通常发生在锁持有时间长，且竞争激烈的场景中，此时应主动禁用自旋锁。
     *
     *
     * 1、可重入锁：在执行对象中所有同步方法不用再次获得锁
     * 2、可中断锁：在等待获取锁过程中可中断
     * 3、公平锁： 按等待获取锁的线程的等待时间进行获取，等待时间长的具有优先获取锁权利
     * 4、读写锁：对资源读取和写入的时候拆分为2部分处理，读的时候可以多线程一起读，写的时候必须同步地写
     *
     *
     */
    fun getLockMap() {
        val map = R.drawable.lock
    }


    /**
     * 虚拟机分区：
     *
     * 方法区：线程独享，记录类的信息
     * 堆：保存对象，GC主战场
     * 程序计数器：线程独享，记录线程的执行位置
     * 虚拟机栈：线程独享，java方法的调用
     * 本地方法栈：线程独享，本地方法的调用
     */

    /**
     * 引用状态
     * 强引用：gc时无法回收，需要手动释放
     * 软引用(SoftReference)：内存不足，即将OOM时，将此软引用指向的对象放入回收范围
     * 弱引用(WeakReference)：gc时进行回收
     * 虚引用(PhantomReference)：
     */

    /**
     * 堆中对象的生命状态
     * 新生代，老年代
     *
     */

}