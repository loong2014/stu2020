package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

/*
https://blog.csdn.net/qq_28114615/article/details/97929524
 */
public class Stu_eventfd extends StuImpl {

    @Override
    public void a_是什么() {
        /*
            eventfd 是Linux 2.6提供的一种系统调用，它可以用来实现事件通知。
            eventfd 包含一个由内核维护的64位无符号整型计数器，创建eventfd时会返回一个文件描述符，
            进程可以通过对这个文件描述符进行read/write来读取/改变计数器的值，从而实现进程间通信。
         */
    }

    @Override
    public void s_数据结构() {
        // 内核维护的 64位无符号整型 计数器
        // eventfd.h
        /*
            typedef uint64_t eventfd_t;

            int eventfd(unsigned int __initial_value, int __flags);

            int eventfd_read(int __fd, eventfd_t* __value);

            int eventfd_write(int __fd, eventfd_t __value);
         */
    }

    @Override
    public void d_怎么用() {
        stu_eventfd();

        stu_eventfd_read();

        stu_eventfd_write();
    }

    private void stu_eventfd() {
        // 创建eventfd
        /*
        int eventfd(unsigned int initial, int flags)

            initial：创建eventfd时它所对应的64位计数器的初始值

            flags：eventfd文件描述符的标志，可由三种选项组成：
                EFD_CLOEXEC表示返回的eventfd文件描述符在fork后exec其他程序时会自动关闭这个文件描述符；
                EFD_NONBLOCK设置返回的eventfd非阻塞；
                EFD_SEMAPHORE表示将eventfd作为一个信号量来使用

            返回值：该eventfd所对应的文件描述符
         */
    }

    private void stu_eventfd_read() {
        // 读eventfd
        /*
            int eventfd_read(int fd, eventfd_t* value) {
                return (read(fd, value, sizeof(*value)) == sizeof(*value)) ? 0 : -1;
            }

           1.read函数会从eventfd对应的64位计数器中读取一个8字节的整型变量；

           2.read函数设置的接收buf的大小不能低于8个字节，否则read函数会出错，errno为EINVAL;

           3.read函数返回的值是按小端字节序的；

           4.如果eventfd设置了EFD_SEMAPHORE，那么每次read就会返回1，并且让eventfd对应的计数器减一；
           如果eventfd没有设置EFD_SEMAPHORE，那么每次read就会直接返回计数器中的数值，read之后计数器就会置0。
           不管是哪一种，当计数器为0时，如果继续read，
                那么read就会阻塞（如果eventfd没有设置EFD_NONBLOCK）
                或者返回EAGAIN错误（如果eventfd设置了EFD_NONBLOCK）。
         */
    }

    private void stu_eventfd_write() {
        // 写eventfd
        /*
            int eventfd_write(int fd, eventfd_t value) {
                return (write(fd, &value, sizeof(value)) == sizeof(value)) ? 0 : -1;
            }

            1.在没有设置EFD_SEMAPHORE的情况下，
                write函数会将发送buf中的数据写入到eventfd对应的计数器中，
                最大只能写入0xffffffffffffffff，否则返回EINVAL错误；

            2.在设置了EFD_SEMAPHORE的情况下，
                write函数相当于是向计数器中进行“添加”，比如说
                    计数器中的值原本是2，如果write了一个3，那么计数器中的值就变成了5。
                如果某一次write后，计数器中的值超过了0xfffffffffffffffe（64位最大值-1），
                那么write就会阻塞直到另一个进程/线程从eventfd中进行了read（如果write没有设置EFD_NONBLOCK），
                    或者返回EAGAIN错误（如果write设置了EFD_NONBLOCK）。
         */
    }
}
