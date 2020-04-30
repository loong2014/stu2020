package com.sunny.other;

/**
 * Created by zhangxin17 on 2020-04-22
 */
public class JavaDemo3 {


    /**
     * 三个线程a、b、c，任务分别是打印"aaa"、"bbb"、"ccc"。如何实现能确保打印顺序是"aaa"->"bbb"->"ccc"？
     * 如果三个线程同时被执行呢？
     * <p>
     * 并发编程线程安全的三个要素——原子性，有序性，可见性
     */
    private static volatile boolean a = false;
    private static volatile boolean b = false;
    private static volatile boolean c = false;

    private static Thread ta = new Thread() {
        @Override
        public void run() {
            super.run();

            while (true) {
                if (a) {
                    System.out.println("aaa");
                    a = false;
                    b = true;
                    break;
                }
            }
        }
    };


    private static Thread tb = new Thread() {
        @Override
        public void run() {
            super.run();

            while (true) {
                if (b) {
                    System.out.println("bbb");
                    b = false;
                    c = true;
                    break;
                }
            }
        }
    };


    private static Thread tc = new Thread() {
        @Override
        public void run() {
            super.run();

            while (true) {
                if (c) {
                    System.out.println("ccc");
                    c = false;
                    break;
                }
            }
        }
    };

    public static void main(String[] args) {

        a = true;
        tc.start();
        tb.start();
        ta.start();
    }
}
