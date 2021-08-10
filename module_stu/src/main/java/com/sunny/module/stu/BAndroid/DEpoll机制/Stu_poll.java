package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

public class Stu_poll extends StuImpl {
    @Override
    public void a_是什么() {
        /*
        select遗留的三个问题中，问题(1)是用法限制问题，问题(2)和(3)则是性能问题。
        poll和select非常相似，poll并没着手解决性能问题，
        poll只是解决了select的问题(1)fds集合大小1024限制问题。

        下面是poll的函数原型，poll改变了fds集合的描述方式，
            使用了pollfd结构而不是select的fd_set结构，使得poll支持的fds集合限制远大于select的1024。
        poll虽然解决了fds集合大小1024的限制问题，但是，
            它并没改变大量描述符数组被整体复制于用户态和内核态的地址空间之间，
            以及个别描述符就绪触发整体描述符集合的遍历的低效问题。
        poll随着监控的socket集合的增加性能线性下降，poll不适合用于大并发场景。
         */
    }

    @Override
    public void s_数据结构() {
        /*
            int poll(struct pollfd *fds,
            nfds_t nfds,
            int timeout
            );
         */
    }
}
