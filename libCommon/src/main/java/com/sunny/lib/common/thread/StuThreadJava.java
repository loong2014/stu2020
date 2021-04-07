package com.sunny.lib.common.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StuThreadJava {


    void stuLock() {

        Lock lock = new ReentrantLock(); // 重入锁，在执行对象中所有同步方法不用再次获得锁

        lock.lock(); // 获取锁，如果锁被暂用，则等待
        lock.unlock(); // 释放锁

        lock.tryLock(); // 注意返回类型是boolean，如果获取锁的时候锁被占用就返回false，否则返回true
        try {
            lock.tryLock(100, TimeUnit.SECONDS); // 比起tryLock()就是给了一个时间期限，保证等待参数时间

            lock.lockInterruptibly(); // 用该锁的获得方式，如果线程在获取锁的阶段进入了等待，那么可以中断此线程，先去做别的事

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void stuSleepVsWait(Object lock) {

        Thread thread = new Thread();
        thread.notifyAll();
        try {
            Thread.sleep(100);

            synchronized (lock) {
                lock.wait();
                lock.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
//        StuThreadJava stuThread = new StuThreadJava();
//        Object lock = new Object();
//        stuThread.stuSleepVsWait(lock);

        ThreadLocal threadLocal = new ThreadLocal<String>();
        threadLocal.set("张三");

        ThreadLocal threadLocal1 = new ThreadLocal<String>();
        threadLocal1.set("李四");


        ThreadLocal threadLocal2 = new ThreadLocal<String>();

        System.out.println(Thread.currentThread().getName() + " , threadLocal  :" + threadLocal.get());
        System.out.println(Thread.currentThread().getName() + " , threadLocal1  :" + threadLocal1.get());
        System.out.println(Thread.currentThread().getName() + " , threadLocal2  :" + threadLocal2.get());
    }
}
