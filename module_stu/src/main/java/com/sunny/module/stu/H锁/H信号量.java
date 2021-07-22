package com.sunny.module.stu.H锁;

import java.util.concurrent.Semaphore;

public class H信号量 {
    public static void main(String[] args) throws InterruptedException {

        //信号量，共享锁，可指定是否为公平锁，初始时设置许可数。
        // 获取时如果无法满足需求，则阻塞，进入CLH队列
        // 释放时如果有等待线程，则通知其尝试获取锁
        Semaphore semaphore = new Semaphore(10,false);

        // 获取/释放一个许可
        semaphore.acquire();
        semaphore.release();

        // 获取/释放多个许可
        semaphore.acquire(3);
        semaphore.release(2);
    }
}
