package com.sunny.module.stu.H锁;

import com.google.gson.internal.UnsafeAllocator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import kotlinx.coroutines.sync.Mutex;

public class HMain {

    //    static int a = 0;
    static AtomicInteger a = new AtomicInteger();

    static Object object = new Object();

    static class MyThread extends Thread {


        MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();


            for (int i = 0; i < 10; i++) {
//                synchronized (MyThread.class) {
//                    a = a + 1;
//                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a.incrementAndGet();
                System.out.println(getName() + " a :" + a);
            }
            synchronized (object) {
                object.notify();
//                object.notifyAll();
            }
            System.out.println(getName() + " , result :" + a);
        }
    }

    static class Sunny {

        // volatile 可见性
        private volatile int index = 0;

    }

    volatile Sunny s;

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new MyThread("t1");

        thread.start();

        System.out.println("main aaaa");
        ReentrantLock lock = new ReentrantLock();
//        lock.lock(); // 加锁
//        lock.unlock(); //解锁


        synchronized (object){
        object.wait();
        System.out.println("main bbbb");

            object.wait(500);
            System.out.println("main cccc");
        }

//        thread.join();
//        new MyThread("zhang").start();
//        new MyThread("li").start();
//        System.out.println("result :" + a);
//
//
        Condition condition = lock.newCondition();
        condition.await(); // 阻塞
        condition.signal(); // 唤醒
//
//        // 重入读写锁
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        rwLock.readLock(); // 读锁
        rwLock.writeLock(); // 写锁
//
//        // 倒计时锁存器，用于并发中，获取多个结果后，对结果进行合并，在回调
        CountDownLatch countDownLatch = new CountDownLatch(2);
//        System.out.println("CountDownLatch  await");
//

        countDownLatch.countDown();
//        countDownLatch.countDown();
//        countDownLatch.await();
//
//        System.out.println("CountDownLatch  finish");
//
//        //循环屏障
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    }
}
