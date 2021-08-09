package com.sunny.module.stu.F类加载器ClassLoader.java;

import com.sunny.module.stu.base.StuImpl;

public class 总结类加载 extends StuImpl {

    @Override
    public void a_是什么() {
        // 加载类器

        // 相关知识
        /*
            类生命周期
         */
    }

    @Override
    public void b_作用() {
        // 负责加载类对象
        // 一个类只会被加载一次
        // 将磁盘上的【class字节码】数据，转换成内存中的class对象

        /*
        由于虚拟机规范对这3点要求并不具体，所以实际的实现是非常灵活的，关于第1点，获取类的二进制字节流（class字节码）就有很多途径：

从ZIP包获取，这是JAR、EAR、WAR等格式的基础
从网络中获取，典型的应用是 Applet
运行时计算生成，这种场景使用最多的是动态代理技术，在 java.lang.reflect.Proxy 类中，就是用了 ProxyGenerator.generateProxyClass 来为特定接口生成形式为 *$Proxy 的代理类的二进制字节流
由其它文件生成，典型应用是JSP，即由JSP文件生成对应的Class类
从数据库中获取等等
所以，动态代理就是想办法，根据接口或目标对象，计算出代理类的字节码，然后再加载到JVM中使用。但是如何计算？如何生成？情况也许比想象的复杂得多，我们需要借助现有的方案。
         */
    }


}
