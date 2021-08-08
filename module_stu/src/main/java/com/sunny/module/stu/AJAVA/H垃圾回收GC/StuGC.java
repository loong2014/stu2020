package com.sunny.module.stu.AJAVA.H垃圾回收GC;

import com.sunny.module.stu.base.StuImpl;

/*
https://blog.csdn.net/laomo_bible/article/details/83112622
 */
public class StuGC extends StuImpl {
    @Override
    public void a_是什么() {

        // 作用区域：堆，方法区
        /*
        jvm 中，程序计数器、虚拟机栈、本地方法栈都是随线程而生随线程而灭，
        栈帧随着方法的进入和退出做入栈和出栈操作，实现了自动的内存清理，因此，
        我们的内存垃圾回收主要集中于 java 堆和方法区中，在程序运行期间，这部分内存的分配和使用都是动态的。
         */

        // GC的对象：没有存活的对象
        /*
        需要进行回收的对象就是已经没有存活的对象，判断一个对象是否存活常用的有两种办法：引用计数和可达分析。

（1）引用计数：每个对象有一个引用计数属性，新增一个引用时计数加1，引用释放时计数减1，计数为0时可以回收。此方法简单，无法解决对象相互循环引用的问题。

（2）可达性分析（Reachability Analysis）：从GC Roots开始向下搜索，搜索所走过的路径称为引用链。当一个对象到GC Roots没有任何引用链相连时，则证明此对象是不可用的。不可达对象。

        在Java语言中，GC Roots包括：

        虚拟机栈中引用的对象。

        方法区中类静态属性实体引用的对象。

        方法区中常量引用的对象。

        本地方法栈中JNI引用的对象
         */

        // 什么时候触发GC
        /*
        (1)程序调用System.gc时可以触发

        (2)系统自身来决定GC触发的时机（根据Eden区和From Space区的内存大小来决定。当内存大小不足时，则会启动GC线程并停止应用线程）

        GC又分为 minor GC 和 Full GC (也称为 Major GC )

Minor GC触发条件：当Eden区满时，触发Minor GC。

Full GC触发条件：

  a.调用System.gc时，系统建议执行Full GC，但是不必然执行

  b.老年代空间不足

  c.方法去空间不足

  d.通过Minor GC后进入老年代的平均大小大于老年代的可用内存

  e.由Eden区、From Space区向To Space区复制时，对象大小大于To Space可用内存，则把该对象转存到老年代，且老年代的可用内存小于该对象大小
         */

        // GC做了什么事
    }
}
