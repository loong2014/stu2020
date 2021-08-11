package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

/*
https://blog.csdn.net/u013319359/article/details/81091176
 */
public class 文件描述符fd extends StuImpl {
    @Override
    public void a_是什么() {
        // 文件描述符，是对文件的描述。在unix里，“一切皆为文件”，设备也会被视为文件。
        /*
        进程：
            file descriptor
            file descriptor table
        系统：
        file table：inode table的索引
        inode table：真正的描述了底层的文件
         */
        /*
        1. unix网络编程中的fd是什么

            1.1 fd全称是file descriptor,是进程独有的文件描述符表的索引

            简单的说，就是内核为每个【进程】维护了一个file descriptor table，
                file descriptor是file descriptor table的索引，
                file descriptor table的表项又转而可以索引到 【系统级】的file table，
            file table又可以索引到系统级的inode table，
            而这个inode table则真正的描述了底层的文件。
            系统级的file table还记录了每个文件被打开的方式：读、写、追加…。
            file descriptor table每个进程都有一个，所以fork的会被拷贝。

            1.2. 对文件描述符执行close操作时，仅仅是关闭了该进程对某文件的访问，其他进程依然能访问
                只有该fd是指向某文件的最后一个fd时，文件对应资源才会被释放。
         */
        /*
        2. 访问文件需要通过系统调用，进程不能直接访问文件

        3. 可以通过/proc/PID/fd/查看PID对应的进程打开了哪些文件

        4. 类Unix系统中，文件描述符还可以指向其他io设备，比如网络连接套接字、管道等
         */
    }
}
