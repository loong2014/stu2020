package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

public class Stu_epoll extends StuImpl {
    @Override
    public void a_是什么() {
        /*
        select遗留的三个问题，问题(1)是比较好解决，poll简单两三下就解决掉了，但是poll的解决有点鸡肋。
        要解决问题(2)和(3)似乎比较棘手，要怎么解决呢？
        我们知道，在计算机行业中，有两种解决问题的思想：
            [1] 计算机科学领域的任何问题, 都可以通过添加一个中间层来解决
            [2] 变集中(中央)处理为分散(分布式)处理
         */

        // （2）fds集合需要从用户空间拷贝到内核空间的问题，我们希望不需要拷贝
        // （3）当被监控的fds中某些有数据可读的时候，我们希望通知更加精细一点，就是我们希望能够
        //      从通知中得到有可读事件的fds列表，而不是需要遍历整个fds来收集。
        fds集合拷贝问题的解决();
        /*
            引入 epoll_ctl 细分对fds集合的操作——
                EPOLL_CTL_ADD
                EPOLL_CTL_MOD
                EPOLL_CTL_DEL
            通过mmap减少用户态和内核太之间的数据交换
         */
    }

    @Override
    public void s_数据结构() {
        //
        /*

         */

    }

    private void fds集合拷贝问题的解决() {
        /*
        fds：文件描述符


        对于IO多路复用，有两件事是必须要做的(对于监控可读事件而言)：
            1. 准备好需要监控的fds集合；
            2. 探测并返回fds集合中哪些fd可读了。
        细看select或poll的函数原型，我们会发现，每次调用select或poll都在重复地准备(集中处理)整个需要监控的fds集合。
        然而对于频繁调用的select或poll而言，fds集合的变化频率要低得多，我们没必要每次都重新准备(集中处理)整个fds集合。


        于是，epoll引入了【epoll_ctl】系统调用，将高频调用的epoll_wait和低频的epoll_ctl隔离开。
        同时，epoll_ctl通过(EPOLL_CTL_ADD、EPOLL_CTL_MOD、EPOLL_CTL_DEL)三个操作来分散对需要监控的fds集合的修改，
        做到了有变化才变更，将select或poll高频、大块内存拷贝(集中处理)变成epoll_ctl的低频、小块内存的拷贝(分散处理)，避免了大量的内存拷贝。

        同时，对于高频epoll_wait的可读就绪的fd集合返回的拷贝问题，
        epoll通过内核与用户空间mmap(内存映射)同一块内存来解决。
        mmap将用户空间的一块地址和内核空间的一块地址同时映射到相同的一块物理内存地址（不管是用户空间还是内核空间都是虚拟地址，最终要通过地址映射映射到物理地址），
        使得这块物理内存对内核和对用户均可见，减少用户态和内核态之间的数据交换。


        另外，epoll通过epoll_ctl来对监控的fds集合来进行增、删、改，那么必须涉及到fd的快速查找问题，
        于是，一个低时间复杂度的增、删、改、查的数据结构来组织被监控的fds集合是必不可少的了。
        在linux 2.6.8之前的内核，epoll使用hash来组织fds集合，于是在创建epoll fd的时候，epoll需要初始化hash的大小。
            于是epoll_create(int size)有一个参数size，以便内核根据size的大小来分配hash的大小。
        在linux 2.6.8以后的内核中，epoll使用红黑树来组织监控的fds集合，
            于是epoll_create(int size)的参数size实际上已经没有意义了。
         */

    }

    private void 按需遍历就绪的fds集合() {
        /*

        引用中间层：
            ready_list：保存被唤醒的sk
            single_epoll_wait_list：保存调用wait的process
        socket唤醒其睡眠队列中的 wait_entry_sk.callback{
            添加sk到ready_list
            唤醒single_epoll_wait_list{
                唤醒process从epoll_wait返回
            }
        }

/////
        通过上面的socket的睡眠队列唤醒逻辑我们知道，
        socket唤醒睡眠在其睡眠队列的wait_entry(process)的时候会调用wait_entry的回调函数callback，
        并且，我们可以在callback中做任何事情。

        为了做到只遍历就绪的fd，我们需要有个地方来组织那些已经就绪的fd。
        为此，epoll引入了一个中间层，
            一个双向链表(ready_list)，
            一个单独的睡眠队列(single_epoll_wait_list)，
        并且，与select或poll不同的是，epoll的process不需要同时插入到多路复用的socket集合的所有睡眠队列中，
        相反process只是插入到中间层的epoll的【单独睡眠队列】中，process睡眠在epoll的单独队列上，等待事件的发生。
        同时，引入一个中间的 【wait_entry_sk】，它与某个socket sk密切相关，
            wait_entry_sk睡眠在sk的睡眠队列上，其callback函数逻辑是
            将当前sk排入到epoll的ready_list中，
            并唤醒epoll的single_epoll_wait_list。
        而single_epoll_wait_list上睡眠的process的回调函数就明朗了：
                遍历ready_list上的所有sk，挨个调用sk的poll函数收集事件，
                然后唤醒process从epoll_wait返回。
         */
    }

    private void EPOLL_CTL_ADD() {
        /*
            [1] 构建睡眠实体wait_entry_sk，将当前socket sk关联给wait_entry_sk，
                并设置wait_entry_sk的回调函数为【epoll_callback_sk】
            [2] 将wait_entry_sk排入当前socket sk的睡眠队列上
         */

        // 回调函数epoll_callback_sk的逻辑如下：
        /*
            epoll_callback_sk{
                [1] 将之前关联的sk排入epoll的ready_list
                [2] 然后唤醒epoll的单独睡眠队列single_epoll_wait_list
            }
         */
    }

    private void epoll_wait() {
        /*
            [1] 构建睡眠实体wait_entry_proc，
                将当前process关联给wait_entry_proc，
                并设置回调函数为【epoll_callback_proc】
            [2] 判断epoll的ready_list是否为空，
                如果为空，则将wait_entry_proc排入epoll的【single_epoll_wait_list】中，
                随后进入schedule循环，这会导致调用epoll_wait的process睡眠。
            [3] wait_entry_proc被事件唤醒或超时醒来，
                wait_entry_proc将被从single_epoll_wait_list移除掉，
                然后wait_entry_proc执行回调函数epoll_callback_proc
         */

        /*
            epoll_callback_proc{
                [1] 遍历epoll的ready_list，挨个调用每个sk的poll逻辑收集发生的事件，
                    对于监控可读事件而言，ready_list上的每个sk都是有数据可读的，
                    这里的遍历是必要的(不同于select/poll的遍历，它不管有没数据可读都需要遍历一些来判断，这样就做了很多无用功。)
                [2] 将每个sk收集到的事件，通过epoll_wait传入的 events 数组回传并唤醒相应的process。
            }
         */
    }

    private void epoll唤醒逻辑() {
        // 整个epoll的协议栈唤醒逻辑如下(对于可读事件而言)：
        /*
            [1] 协议数据包到达网卡并被排入socket sk的接收队列
            [2] 睡眠在sk的睡眠队列wait_entry被唤醒，wait_entry_sk的回调函数epoll_callback_sk被执行
            [3] epoll_callback_sk将当前sk插入epoll的ready_list中
            [4] 唤醒睡眠在epoll的单独睡眠队列single_epoll_wait_list的wait_entry，
                wait_entry_proc被唤醒执行回调函数epoll_callback_proc
            [5] 遍历epoll的ready_list，挨个调用每个sk的poll逻辑收集发生的事件
            [6] 将每个sk收集到的事件，通过epoll_wait传入的events数组回传并唤醒相应的process。
         */

        /*
            epoll巧妙的引入一个中间层解决了大量监控socket的无效遍历问题。
            细心的同学会发现，epoll在中间层上为每个监控的【socket】准备了一个【单独】的回调函数【epoll_callback_sk】，
                而对于select/poll，所有的socket都公用一个相同的回调函数。
            正是这个单独的回调epoll_callback_sk使得每个socket都能单独处理自身，
                当自己就绪的时候将自身socket挂入epoll的ready_list。
            同时，epoll引入了一个睡眠队列single_epoll_wait_list，分割了两类睡眠等待。
                process不再睡眠在所有的socket的睡眠队列上，
                而是睡眠在epoll的睡眠队列上，在等待”任意一个socket可读就绪”事件。
            而中间wait_entry_sk则代替process睡眠在具体的socket上，当socket就绪的时候，它就可以处理自身了。


         */
    }

    private void 两种触发模式() {
        // 阻塞模式下，不能使用 ET
        // epoll不允许重复ADD的，除非先DEL了，再ADD
        ET边沿触发();

        LT水平触发();

        ETvsLT总结();

        /*
        我们来回顾一下，5.2节（3）epoll唤醒逻辑 的第五个步骤
            [5] 遍历epoll的ready_list，挨个调用每个sk的poll逻辑收集发生的事件

        大家是不是有个疑问呢：挂在ready_list上的sk什么时候会被移除掉呢？
        其实，sk从ready_list【移除的时机】正是区分两种事件模式的本质。
        因为，通过上面的介绍，我们知道ready_list是否为空是epoll_wait是否返回的条件。
        于是，在两种事件模式下，步骤5如下：
        ET：
            [5] 遍历epoll的ready_list，将sk从ready_list中移除，然后调用该sk的poll逻辑收集发生的事件
        LT：
            [5.1] 遍历epoll的ready_list，将sk从ready_list中移除，然后调用该sk的poll逻辑收集发生的事件
            [5.2] 如果该sk的poll函数返回了关心的事件(对于可读事件来说，就是POLL_IN事件)，
                那么该sk被重新加入到epoll的ready_list中。


        对于可读事件而言：
        在ET模式下，如果某个socket有新的数据到达，那么该sk就会被排入epoll的ready_list，
            从而epoll_wait就一定能收到可读事件的通知(调用sk的poll逻辑一定能收集到可读事件)。
            于是，我们通常理解的缓冲区状态变化(从无到有)的理解是不准确的，准确的理解应该是是否有新的数据达到缓冲区。

        而在LT模式下，某个sk被探测到有数据可读，那么该sk会被重新加入到read_list，那么在该sk的数据被全部取走前，
            下次调用epoll_wait就一定能够收到该sk的可读事件(调用sk的poll逻辑一定能收集到可读事件)，从而epoll_wait就能返回。

        通过上面的概念介绍，我们知道对于可读事件而言，LT比ET多了两个操作：
            (1)对ready_list的遍历的时候，对于收集到可读事件的sk会重新放入ready_list；
            (2)下次epoll_wait的时候会再次遍历上次重新放入的sk，
                如果sk本身没有数据可读了，那么这次遍历就变得多余了。

        在服务端有海量活跃socket的时候，LT模式下，epoll_wait返回的时候，会有海量的socket sk重新放入ready_list。
            如果，用户在第一次epoll_wait返回的时候，将有数据的socket都处理掉了，那么下次epoll_wait的时候，
            上次epoll_wait重新入ready_list的sk被再次遍历就有点多余，这个时候LT确实会带来一些性能损失。
            然而，实际上会存在很多多余的遍历么？

        先不说第一次epoll_wait返回的时候，用户进程能否都将有数据返回的socket处理掉。在用户处理的过程中，
            如果该socket有新的数据上来，那么协议栈发现sk已经在ready_list中了，那么就不需要再次放入ready_list，
            也就是在LT模式下，对该sk的再次遍历不是多余的，是有效的。
            同时，我们回归epoll高效的场景在于，服务器有海量socket，但是活跃socket较少的情况下才会体现出epoll的高效、高性能。
            因此，在实际的应用场合，绝大多数情况下，ET模式在性能上并不会比LT模式具有压倒性的优势，
            至少，目前还没有实际应用场合的测试表面ET比LT性能更好。
         */

        // ET vs LT - 复杂度
        /*
            我们知道，对于可读事件而言，在阻塞模式下，是无法识别队列空的事件的，
            并且，事件通知机制，仅仅是通知有数据，并不会通知有多少数据。
            于是，在阻塞模式下，在epoll_wait返回的时候，
            我们对某个socket_fd调用recv或read读取并返回了一些数据的时候，我们不能再次直接调用recv或read，
            因为，如果socket_fd已经【无数据可读】的时候，进程就会【阻塞】在该socket_fd的recv或read调用上，
            这样就影响了IO多路复用的逻辑(我们希望是【阻塞】在所有被监控socket的【epoll_wait】调用上，
            而【不是】单独某个【socket_fd】上)，造成其他socket饿死，即使有数据来了，也无法处理。


            接下来，我们只能再次调用epoll_wait来探测一些socket_fd，看是否还有数据可读。
            在LT模式下，如果socket_fd还有数据可读，那么epoll_wait就一定能够返回，
                接着，我们就可以对该socket_fd调用recv或read读取数据。
            然而，在ET模式下，尽管socket_fd还是数据可读，但是如果没有新的数据上来，那么epoll_wait是不会通知可读事件的。
                这个时候，epoll_wait阻塞住了，这下子坑爹了，明明有数据你不处理，非要等新的数据来了在处理，
                那么我们就死扛咯，看谁先忍不住。


            等等，在阻塞模式下，不是不能用ET的么？
                是的，正是因为有这样的缺点，ET强制需要在非阻塞模式下使用。
            在ET模式下，epoll_wait返回socket_fd有数据可读，我们必须要读完所有数据才能离开。
                因为，如果不读完，epoll不会在通知你了，虽然有新的数据到来的时候，会再次通知，
                但是我们并不知道新数据会不会来，以及什么时候会来。
            由于在阻塞模式下，我们是无法通过recv/read来探测空数据事件，于是，
            我们必须采用非阻塞模式，一直read直到EAGAIN。因此，ET要求socket_fd非阻塞也就不难理解了。


            另外，epoll_wait原本的语意是：监控并探测socket是否有数据可读(对于读事件而言)。
            LT模式保留了其原本的语意，只要socket还有数据可读，它就能不断反馈，
                于是，我们想什么时候读取处理都可以，我们永远有再次poll的机会去探测是否有数据可以处理，
                这样带来了编程上的很大方便，不容易死锁造成某些socket饿死。

            相反，ET模式修改了epoll_wait原本的语意，变成了：监控并探测socket是否有新的数据可读。

            于是，在epoll_wait返回socket_fd可读的时候，我们需要小心处理，要不然会造成死锁和socket饿死现象。
                典型如listen_fd返回可读的时候，我们需要不断的accept直到EAGAIN。
            假设同时有三个请求到达，epoll_wait返回listen_fd可读，这个时候，
                如果仅仅accept一次拿走一个请求去处理，那么就会留下两个请求，如果这个时候一直没有新的请求到达，
                那么再次调用epoll_wait是不会通知listen_fd可读的，
                于是epoll_wait只能睡眠到超时才返回，
                遗留下来的两个请求一直得不到处理，处于饿死状态。
         */
    }

    private void LT水平触发() {
        // Level Triggered (LT) 水平触发
        /*
            .socket接收缓冲区不为空，有数据可读，则读事件一直触发
            .socket发送缓冲区不满可以继续写入数据，则写事件一直触发

            符合思维习惯，epoll_wait返回的事件就是socket的状态

         */
    }

    private void ET边沿触发() {
        // Edge Triggered (ET) 边沿触发
        /*
            仅在缓冲区状态变化时触发事件，比如数据缓冲去从无到有的时候(不可读-可读)

            .socket的接收缓冲区状态变化时触发读事件，即空的接收缓冲区刚接收到数据时触发读事件
            .socket的发送缓冲区状态变化时触发写事件，即满的缓冲区刚空出空间时触发读事件
         */
    }

    private void ETvsLT总结() {

        // 最后总结一下，ET和LT模式下epoll_wait返回的条件
        /*
        ET - 对于读操作
            [1] 当接收缓冲buffer内待读数据增加的时候时候(由空变为不空的时候、或者有新的数据进入缓冲buffer)
            [2] 调用epoll_ctl(EPOLL_CTL_MOD)来改变socket_fd的监控事件，
                也就是重新mod socket_fd的EPOLL_IN事件，并且接收缓冲buffer内还有数据没读取。
                (这里不能是EPOLL_CTL_ADD的原因是，epoll不允许重复ADD的，除非先DEL了，再ADD)
            因为epoll_ctl(ADD或MOD)会调用sk的poll逻辑来检查是否有关心的事件，如果有，
            就会将该sk加入到epoll的ready_list中，下次调用epoll_wait的时候，就会遍历到该sk，然后会重新收集到关心的事件返回。

        ET - 对于写操作
            [1] 发送缓冲buffer内待发送的数据减少的时候(由满状态变为不满状态的时候、或者有部分数据被发出去的时候)
            [2] 调用epoll_ctl(EPOLL_CTL_MOD)来改变socket_fd的监控事件，
                也就是重新mod socket_fd的EPOLL_OUT事件，并且发送缓冲buffer还没满的事件。

        LT - 对于读操作
            LT就简单多了，唯一的条件就是，接收缓冲buffer内有可读数据的时候

        LT - 对于写操作
            LT就简单多了，唯一的条件就是，发送缓冲buffer还没满的时候

        在绝大多少情况下，ET模式并不会比LT模式更为高效，同时，ET模式带来了不好理解的语意，这样容易造成编程上面的复杂逻辑和坑点。
        因此，建议还是采用LT模式来编程更为舒爽。
         */
    }
}
