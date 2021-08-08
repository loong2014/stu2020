package com.sunny.module.stu.AJAVA.G多线程;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GThread {

    static class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 线程生命周期：创建、就绪、运行、阻塞、销毁

        // 创建线程的方式：
        new MyThread().start(); // 继承Thread

        Thread thread = new Thread(new MyRunnable());
        thread.start(); // 实现Runnable

        //线程池
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1000, false);

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler executionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // TODO: 2021/7/25 异常处理
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5, // 核心线程个数
                100, //线程池最大值
                1000, TimeUnit.MILLISECONDS,//空闲线程销毁时间
                queue, // 线程池类型
                threadFactory, // 线程工厂
                executionHandler // 执行时异常处理
        );
        executor.execute(new MyRunnable()); // 线程池

        //
        String name = "张三";
        ThreadLocal<String> userLocal = new ThreadLocal<>();
        userLocal.set(name);

        int age = 18;
        ThreadLocal<Integer> ageLocal = new ThreadLocal<>();
        ageLocal.set(age);

        System.out.println("userLocal :" + userLocal.get());


        //
        ReferenceQueue<String> rq = new ReferenceQueue<>();
        rq.poll();
    }
}
