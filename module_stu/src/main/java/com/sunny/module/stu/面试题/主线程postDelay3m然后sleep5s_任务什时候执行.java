package com.sunny.module.stu.面试题;

import com.sunny.module.stu.base.StuImpl;

public class 主线程postDelay3m然后sleep5s_任务什时候执行 extends StuImpl {

    @Override
    public void a_是什么() {
        /*
            主线程中postDelay(3s)一个任务，然后sleep 5s
            Q：任务什么时候执行？
            A：5s后执行
            **** android 中，如果在UI线程耗时超过5s，会出现anr异常
                单也不是必然的，如果此时没有其它任务需要在UI线程执行，那就不会anr。
                anr的本质，是方法执行时间超过5s
            所以这个问题的答案：
            可能5s后执行，也可能，5s后发生anr
         */
    }
}
