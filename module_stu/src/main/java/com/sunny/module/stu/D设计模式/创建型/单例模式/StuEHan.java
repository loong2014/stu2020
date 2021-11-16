package com.sunny.module.stu.D设计模式.创建型.单例模式;

import com.sunny.module.stu.base.StuImpl;

public class StuEHan extends StuImpl {
    @Override
    public void c_功能() {
        // 饿汉式（静态常量）【可用】
        // 饿汉式（静态代码块）【可用】
        // 懒汉式（线程不安全）【不可用】
        // 懒汉式（线程安全，同步方法）【不推荐用】
        // 懒汉式（线程安全，同步代码块）【不可用】
        /*
            private static Singleton singleton;

            public static Singleton getInstance() {
                if (singleton == null) {
                    synchronized (Singleton.class) {
                        singleton = new Singleton();
                    }
                }
                return singleton;
            }
         */
        // 双重检查【推荐使用】
        /*
            private static volatile Singleton singleton;

            public static Singleton getInstance() {
                if (singleton == null) {
                    synchronized (Singleton.class) {
                        if (singleton == null) {
                            singleton = new Singleton();
                        }
                    }
                }
                return singleton;
            }
         */

        // 静态内部类【推荐使用】
        /*
            private static class SingletonInstance {
                private static final Singleton INSTANCE = new Singleton();
            }

            public static Singleton getInstance() {
                return SingletonInstance.INSTANCE;
            }
         */

        // 枚举【推荐使用】
        /*
            enum Singleton {
                INSTANCE;
            }
         */
    }
}

enum Singleton {
    INSTANCE;
}
