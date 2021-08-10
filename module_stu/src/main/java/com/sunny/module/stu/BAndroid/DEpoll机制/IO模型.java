package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

/*
https://cloud.tencent.com/developer/article/1005481
 */
public class IO模型 extends StuImpl {
    @Override
    public void a_是什么() {
        /*
            [1] blocking IO - 阻塞IO
            [2] nonblocking IO - 非阻塞IO
            [3] IO multiplexing - IO多路复用
            [4] signal driven IO - 信号驱动IO
            [5] asynchronous IO - 异步IO

            其中前面4种IO都可以归类为synchronous IO - 同步IO，

            select、poll、epoll 三个都是IO多路复用的机制，
            可以监视多个描述符的读/写等事件，一旦某个描述符就绪（一般是读或者写事件发生了），
            就能够将发生的事件通知给关心的应用程序去处理该事件。
         */
        阻塞IO();
        非阻塞IO();
        IO多路复用();
        异步IO();
    }

    @Override
    public void s_数据结构() {
        /*
        下面以network IO中的read读操作为切入点，来讲述同步（synchronous） IO和异步（asynchronous） IO、阻塞（blocking） IO和非阻塞（non-blocking）IO的异同。
        一般情况下，一次网络IO读操作会涉及
        两个系统对象：
            (1) 用户进程(线程)Process
            (2)内核对象kernel

        两个处理阶段：
            [1] Waiting for the data to be ready - 等待数据准备好
            [2] Copying the data from the kernel to the process - 将数据从内核空间的buffer拷贝到用户空间进程的buffer

        IO模型的异同点就是区分在这两个系统对象、两个处理阶段的不同上。
         */
    }

    private void 阻塞IO() {
        /*
        // 两个处理阶段 一起 阻塞
        Process 请求【1】阻塞 -> 【2】阻塞 -> 返回

        如上图所示，用户进程process在Blocking IO读recvfrom操作的两个阶段都是等待的。
        在数据没准备好的时候，process原地等待kernel准备数据。
        kernel准备好数据后，process继续等待kernel将数据copy到自己的buffer。
        在kernel完成数据的copy后process才会从recvfrom系统调用中返回。
         */
    }

    private void 非阻塞IO() {
        /*
        // 第二个处理阶段阻塞
        // 1
        Process 请求【1】not ready 返回

        // 2
        Process 请求【1】ready -> 【2】阻塞 -> 返回

        从图中可以看出，process在NonBlocking IO读recvfrom操作的第一个阶段是不会block等待的，
        如果kernel数据还没准备好，那么recvfrom会立刻返回一个EWOULDBLOCK错误。
        当kernel准备好数据后，进入处理的第二阶段的时候，process会等待kernel将数据copy到自己的buffer，
        在kernel完成数据的copy后process才会从recvfrom系统调用中返回。

        使用时，需要不停的进行轮训数据是否准备好
         */
    }

    private void IO多路复用() {

        /*
        IO多路复用机制的本质：linux(2.6+)内核的事件wakeup callback机制
         */
        /*
        // 两个处理阶段 分开 阻塞

        Process 请求【1】阻塞 -> 返回【1】结果
        Process 请求【2】阻塞 -> 返回【2】结果

        IO多路复用，就是我们熟知的select、poll、epoll模型。从图上可见，在IO多路复用的时候，
        process在两个处理阶段都是block住等待的。
        初看好像IO多路复用没什么用，其实select、poll、epoll的
        优势在于可以以较少的代价来同时监听处理多个IO。
         */
    }

    private void 异步IO() {
        /*
        // 不存在阻塞
        Process 请求【1】not ready 返回
        【1】ready【2】ready 通知Process
        Process 请求直接获取结果

        异步IO要求process在recvfrom操作的两个处理阶段上都不能等待，
        也就是process调用recvfrom后立刻返回，
        kernel自行去准备好数据并将数据从kernel的buffer中copy到process的buffer，
        再通知process读操作完成了，然后process在去处理。
        遗憾的是，linux的网络IO中是不存在异步IO的，linux的网络IO处理的第二阶段总是阻塞等待数据copy完成的。
        真正意义上的网络异步IO是Windows下的IOCP（IO完成端口）模型。
         */
    }
}
