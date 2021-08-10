package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

public class Stu_select extends StuImpl {


    @Override
    public void a_是什么() {
        /*
        在一个高性能的网络服务上，大多情况下一个服务进程(线程)process需要同时处理多个socket，
        我们需要公平对待所有socket，对于read而言，那个socket有数据可读，process就去读取该socket的数据来处理。
        于是对于read，一个朴素的需求就是关心的N个socket是否有数据”可读”，
        也就是我们期待”可读”事件的通知，而不是盲目地对每个socket调用recv/recvfrom来尝试接收数据。
        我们应该block在等待事件的发生上，这个事件简单点就是”关心的N个socket中一个或多个socket有数据可读了”，
        当block解除的时候，就意味着，我们一定可以找到一个或多个socket上有可读的数据。
        另一方面，根据socket wakeup callback机制，我们不知道什么时候，哪个socket会有读事件发生，
        于是，process需要同时插入到这N个socket的sleep_list上等待任意一个socket可读事件发生而被唤醒，
        当process被唤醒的时候，其callback里面应该有个逻辑去检查具体那些socket可读了。

        于是，select的多路复用逻辑就清晰了，
        select 为每个 socket 引入一个poll逻辑，该poll逻辑用于收集socket发生的事件


        1、在所有的 socket 的 sleep_list 中添加一个callback，等待 socket可读事件的发生
        2、callback 被回调后，遍历所有 socket 读数据
         */
    }

    @Override
    public void s_数据结构() {
        /*
        5个参数，后面4个参数都是in/out类型(值可能会被修改返回)

        int select(int nfds,
                fd_set *readfds,
                fd_set *writefds,
                fd_set *exceptfds,
                struct timeval *timeout
                );
         */
    }

    @Override
    public void c_功能() {
        /*
        poll()
        {
            //其他逻辑
            if (recieve queque is not empty)
            {
                sk_event |= POLL_IN；
            }
           //其他逻辑
        }

         */
        /*
        当用户process调用select的时候，select会将需要监控的readfds集合拷贝到内核空间（假设监控的仅仅是socket可读），
        然后遍历自己监控的socket sk，挨个调用sk的poll逻辑以便检查该sk是否有可读事件，遍历完所有的sk后，如果没有任何一个sk可读，
        那么select会调用schedule_timeout进入schedule循环，使得process进入睡眠。
        如果在timeout时间内某个sk上有数据可读了，或者等待timeout了，则调用select的process会被唤醒，
        接下来select就是遍历监控的sk集合，挨个收集可读事件并返回给用户了，相应的伪码如下：
         */

        /*
        for (sk in readfds)
        {
            sk_event.evt = sk.poll();
            sk_event.sk = sk;
            ret_event_for_process;
        }
         */
    }

    @Override
    public void l_限制() {
        /*
        [1] 被监控的fds需要从用户空间拷贝到内核空间
            为了减少数据拷贝带来的性能损坏，内核对被监控的fds集合大小做了限制，
            并且这个是通过宏控制的，大小不可改变(限制为1024)。
        [2] 被监控的fds集合中，只要有一个有数据可读，整个socket集合就会被遍历一次调用sk的poll函数收集可读事件
            由于当初的需求是朴素，仅仅关心是否有数据可读这样一个事件，当事件通知来的时候，由于数据的到来是异步的，
            我们不知道事件来的时候，有多少个被监控的socket有数据可读了，于是，只能挨个遍历每个socket来收集可读事件。


        到这里，我们有三个问题需要解决：
            用法限制问题
            （1）被监控的fds集合限制为1024，1024太小了，我们希望能够有个比较大的可监控fds集合

            性能问题
            （2）fds集合需要从用户空间拷贝到内核空间的问题，我们希望不需要拷贝
            （3）当被监控的fds中某些有数据可读的时候，我们希望通知更加精细一点，就是我们希望能够
                    从通知中得到有可读事件的fds列表，而不是需要遍历整个fds来收集。
         */
    }
}
