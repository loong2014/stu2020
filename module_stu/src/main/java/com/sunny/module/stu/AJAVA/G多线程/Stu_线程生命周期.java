package com.sunny.module.stu.AJAVA.G多线程;

import com.sunny.module.stu.base.StuImpl;

public class Stu_线程生命周期 extends StuImpl {

    Thread thread = new Thread();

    @Override
    public void a_是什么() {
        // 【1】新建

        // 【2】就绪

        // 【3】运行

        // 【4】阻塞

        // 【5】销毁
    }

    public void stu_状态转换() throws InterruptedException {

        //
        Thread.sleep(100);
        // sleep    3 -sleep-> 4 -sleep结束-> 2

        Thread.yield();
        // yield    3 -> 2

        thread.join(); // 等待当前线程结束后，再执行
        // join     3 -等待另一个线程结束-> 4 -另一个线程执行结束-> 2

        thread.interrupt();
        // interrupt 3 -> 5
    }
}
