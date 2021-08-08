package com.sunny.module.stu.AJAVA.G多线程;

import com.sunny.module.stu.base.StuImpl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stu_ReentrantLock extends StuImpl {
    @Override
    public void a_是什么() {
        // 重入锁
        ReentrantLock reentrantLock = new ReentrantLock(false);

        // ConditionObject
        Condition condition = reentrantLock.newCondition();

        try {
            condition.await(); // 阻塞

            condition.signal(); // 取消阻塞

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
