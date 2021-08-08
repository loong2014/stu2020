package com.sunny.module.stu.AJAVA.G多线程;

import com.sunny.module.stu.base.StuImpl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Stu锁 extends StuImpl {

    @Override
    public void a_是什么() {
        // 线程安全的三要素：
        /*
            原子性
            可见性
            有序性
         */
        // 锁类型
        /*
            重入锁，非重入锁

            公平锁，非公平锁

            独占锁，共享锁

            悲观锁，乐观锁
         */

        // 具体实现
        synchronized (this) {

        }

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
        /*
            ReentrantLock(fair)：重入锁，通过参数配置是否是公平锁，独占锁，乐观锁

            synchronized：重入锁，非公平锁，独占锁，乐观锁

            ReadWriteLock(fair)：重入锁，通过参数配置是否是公平锁，乐观锁
                读锁：共享锁
                写锁：独占锁

         */
    }

    @Override
    public void c_功能() {
        // 确保资源的安全

        // 常见的实现方式
        /*
            synchronized 关键字
            Lock 对象
                ReentrantLock // 重入锁

         */


    }

    private void stu_synchronized() {
        // 锁状态
        /*
            无锁  偏向锁     轻量级锁    重量级锁
         */

    }

    private void stu_死锁() {
        /*
            1、互斥，同一时刻，资源只能被一个线程占有
            2、不可剥夺，即当资源被其它线程占用时，只有由占有线程进行释放，其它线程无法剥夺资源
            3、线程获取资源后长时间占用资源，使其它线程无法获取资源
                当不使用资源时，要即时释放
            4、多线程互相持有对方要获取的资源，同时还想获取对方持有的资源，造成循环等待
         */
    }
}
