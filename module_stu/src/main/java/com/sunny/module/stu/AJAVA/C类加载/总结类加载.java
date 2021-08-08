package com.sunny.module.stu.AJAVA.C类加载;

import com.sunny.module.stu.base.StuImpl;

public class 总结类加载 extends StuImpl {

    @Override
    public void a_是什么() {
        // 加载类器

        // 相关知识
        /*
            类生命周期
         */
        try {
            stu_loadClass();

            stu_findClass();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void c_功能() {
        // 获取
        // getClass().getClassLoader()

        Java中的类加载器();

        // 双亲委派模型
        /*
            当加载一个类时，先调用其父加载器加载，父类加载失败是，才自己加载。
         */


    }

    private void stu_loadClass() throws ClassNotFoundException {
        getClass().getClassLoader().loadClass("");

        // loadClass的方法实现

        /*
        // First, check if the class has already been loaded
        // 【1】findLoadedClass 判断当前类是否已经被加载
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            try {
                if (parent != null) {
        // 【2】调用父类的loadClass
                    c = parent.loadClass(name, false);
                } else {
        // 【3】调用根加载器loadClass
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                // ClassNotFoundException thrown if class not found
                // from the non-null parent class loader
            }

            if (c == null) {
                // If still not found, then invoke findClass in order
                // to find the class.
        // 【4】当前类进行类加载
                c = findClass(name);
            }
        }
        // 【5】返回类对象
        return c;
         */
    }


    private void stu_defineClass() {
/*
    private Class<?> defineClass(String name, Resource res) throws IOException {
        long t0 = System.nanoTime();
        int i = name.lastIndexOf('.');
        // 【1】根据name获取源码地址
        URL url = res.getCodeSourceURL();
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // Check if package already loaded.
            Manifest man = res.getManifest();
            definePackageInternal(pkgname, man, url);
        }
        // Now read the class bytes and define the class
        // 【2】获取class的二进制字节码
        java.nio.ByteBuffer bb = res.getByteBuffer();
        if (bb != null) {
            // Use (direct) ByteBuffer:
            // 【3】获取签名信息
            CodeSigner[] signers = res.getCodeSigners();
            // 【4】获取源码信息
            CodeSource cs = new CodeSource(url, signers);
            // Android-removed: Android doesn't use sun.misc.PerfCounter.
            // sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            //
            return defineClass(name, bb, cs);
        } else {
            byte[] b = res.getBytes();
            // must read certificates AFTER reading bytes.
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            // Android-removed: Android doesn't use sun.misc.PerfCounter.
            // sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineClass(name, b, 0, b.length, cs);
        }
    }
 */

    }
    private void stu_findClass() {

        // 调用 defineClass 获取Class对象

        /*
        final Class<?> result;
        try {
            result = AccessController.doPrivileged(
                new PrivilegedExceptionAction<Class<?>>() {
                    public Class<?> run() throws ClassNotFoundException {
                        // 【1】路径转换
                        String path = name.replace('.', '/').concat(".class");
                        // 【2】 URLClassPath ucp，获取
                        Resource res = ucp.getResource(path, false);
                        if (res != null) {
                            try {
                                // 【3】核心是调用 defineClass
                                return defineClass(name, res);
                            } catch (IOException e) {
                                throw new ClassNotFoundException(name, e);
                            }
                        } else {
                            return null;
                        }
                    }
                }, acc);
        } catch (java.security.PrivilegedActionException pae) {
            throw (ClassNotFoundException) pae.getException();
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
         */

        // URLClassPath
    }

    @Override
    public void s_面试点() {
        // Q：class.forName() 与 classLoader.loadClass() 的区别？
        // A：class.forName() = classLoader.loadClass() + 类初始化

    }

    private void Java中的类加载器() {
        // 根加载器：BootStrap
        // 加载java的核心类

        // 扩展类加载器：Extension
        // 加载java的扩展类

        // 应用加载器：SystemApp
        // 加载当前应用的类
    }

}
