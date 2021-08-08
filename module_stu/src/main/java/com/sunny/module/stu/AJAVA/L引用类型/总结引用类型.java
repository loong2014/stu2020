package com.sunny.module.stu.AJAVA.L引用类型;

import com.sunny.module.stu.AJAVA.BaseSunny;
import com.sunny.module.stu.base.StuImpl;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class 总结引用类型 extends StuImpl {

    @Override
    public void a_是什么() {
        // 强 软 弱 虚
        // 引用队列
    }

    @Override
    public void c_功能() {
        BaseSunny sunny = new BaseSunny("张三");

        // 强 永远不会被回收
        BaseSunny s1 = sunny;

        // 软 内存不足时回收
        SoftReference<BaseSunny> s2 = new SoftReference<>(sunny);
        /*
            缓存数据
         */

        // 弱 gc时回收
        WeakReference<BaseSunny> s3 = new WeakReference<>(sunny);
        /*
            ThreadLocal
            当对象只剩下弱引用时，则被回收，以免造成内存泄漏
         */

        // 虚 gc时回收，get永远为null，必须解决引用队列使用
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<BaseSunny> s4 = new PhantomReference<>(sunny, referenceQueue);
        /*
            当虚引用被回收时，会添加到引用队列，可以通过监听引用队列的变化，来做一下操作
            比如，释放对应的【直接内存】
         */
        // 【直接内存】
        /*
            JVM直接指向JVM内存空间之外的一块内存空间，GC是无法回收JVM以外的内存，只能手动释放
         */
    }
}
