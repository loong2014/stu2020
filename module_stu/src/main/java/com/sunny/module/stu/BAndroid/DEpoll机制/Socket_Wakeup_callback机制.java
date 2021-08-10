package com.sunny.module.stu.BAndroid.DEpoll机制;

import com.sunny.module.stu.base.StuImpl;

public class Socket_Wakeup_callback机制 extends StuImpl {

    @Override
    public void a_是什么() {
        /*
        linux(2.6+)内核的事件wakeup callback机制，这是IO多路复用机制存在的本质。
        Linux通过socket【睡眠队列】来管理所有等待socket的某个事件的process，
        同时通过wakeup机制来异步唤醒整个【睡眠队列】上等待事件的process，通知process相关事件发生。
        通常情况，socket的事件发生的时候，其会顺序遍历socket睡眠队列上的每个process节点，
        调用每个process节点挂载的callback函数。在遍历的过程中，
        如果遇到某个节点是排他的，那么就终止遍历。
        总体上会涉及两大逻辑：
        （1）睡眠等待逻辑：涉及select、poll、epoll_wait的阻塞等待逻辑
        （2）唤醒逻辑：
         */

        睡眠等待逻辑();

        唤醒逻辑();
    }

    private void 睡眠等待逻辑() {
        /*
        [1]select、poll、epoll_wait陷入内核，判断监控的socket是否有关心的事件发生了，
            如果没，则为当前process构建一个wait_entry节点，然后插入到监控socket的sleep_list
        [2]进入循环的schedule直到关心的事件发生了
        [3]关心的事件发生后，将当前process的wait_entry节点从socket的sleep_list中删除。
         */
    }

    private void 唤醒逻辑() {
        /*
        [1]socket的事件发生了，然后socket顺序遍历其睡眠队列，依次调用每个wait_entry节点的callback函数
        [2]直到完成队列的遍历或遇到某个wait_entry节点是排他的才停止。
        [3]一般情况下callback包含两个逻辑：
            1.wait_entry自定义的私有逻辑；
            2.唤醒的公共逻辑，主要用于将该wait_entry的process放入CPU的就绪队列，让CPU随后可以调度其执行。
         */
    }

}

