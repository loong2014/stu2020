package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

/*
https://blog.csdn.net/l477918269/article/details/96323624?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control
 */
public class Stu_pipe extends StuImpl {

    @Override
    public void a_是什么() {
        // pipe:中文意思是管道,使用I/O流操作,实现跨进程通信,管道的一端的读,另一端写,标准的生产者消费者模式
        // 创建管道时，在内核中开辟一块缓冲区（称为管道）用于通信，它有一个读端一个写端

        // 管道的大小
        /*
        在Linux中，管道是一种使用非常频繁的通信机制。从本质上说，管道也是一种文件，但它又和一般的文件有所不同，
        管道可以克服使用文件进行通信的两个问题，具体表现为：

        1、限制管道的大小。
            实际上，管道是一个固定大小的缓冲区。在Linux中，该缓冲区的大小为1页，即4K字节，使得它的大小不象文件那样不加检验地增长。
            使用单个固定缓冲区也会带来问题，比如在写管道时可能变满，当这种情况发生时，
            随后对管道的write()调用将默认地被阻塞，等待某些数据被读取，以便腾出足够的空间供write()调用写。

        2、读取进程也可能工作得比写进程快。
            当所有当前进程数据已被读取时，管道变空。当这种情况发生时，一个随后的read()调用将默认地被阻塞，
            等待某些数据被写入，这解决了read()调用返回文件结束的问题。
         */

        // 匿名管道
        /*
            //
            管道作用于有血缘关系的进程之间,通过fork来传递,意味着，这些管道只可用于同一机器的进程间通信
                1.只能用于具有共同祖先的进程（具有亲缘关系的进程）之间进行通信
                2.管道提供的是半双工的服务，是单向的
                3.管道是面向字节流服务的（连续的，无边界）
                4.文件生命周期随进程，进程结束了，管道也就释放了。


            // 创建
            int fd[2] = {0};
            pipe(fd);
                fd[0]指向管道的读端
                fd[1]指向管道的写端

            // 读
            close(fd[1]); // 关闭写
            char ret[1024];
            ssize_t s = read(fd[0], ret, sizeof(ret) - 1);

            // 写
            close(fd[0]); // 关闭读
            char buf[] = "I'm child";
            write(fd[1], buf, strlen(buf));
            close(fd[1]); // 写完之后要关闭
         */

        // 命名管道
        /*
            如果我们想在不相关的进程之间交换数据，就需要用到FIFO文件，被称为命名管道。
                不同进程根据名称打开fifo文件，共享同一个inode，这保证了两个文件操作的是同一块磁盘空间

            // 创建
            int mkfifo(const char* filename, mode_t mode)
                filename：管道命名
                mode：权限

            // 读
            int op = open("ALA", O_RDONLY);//打开匿名管道
            char buf[1024];
            ssize_t s = read(op, buf, sizeof(buf) - 1);//从管道中读数据

            // 写
            int op = open("ALA", O_WRONLY);//打开命名管道
            char buf[1024];
            write(op, buf, strlen(buf));//写入数据到管道中
         */

        // 命名管道与匿名管道的区别
        /*
            创造方式不同：
                命名管道由mkfifo函数创建
                匿名管道由pipe函数创建

            打开方式：
                命名管道通过open打开
                匿名管道通过read打开

            适用范围不同：
                命名管道可以实现不同进程间的通信
                匿名管道只能实现有亲缘关系进程间的关系
         */
    }
}
