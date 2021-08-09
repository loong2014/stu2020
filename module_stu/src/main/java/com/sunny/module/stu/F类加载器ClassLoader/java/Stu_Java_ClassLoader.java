package com.sunny.module.stu.F类加载器ClassLoader.java;

import com.sunny.module.stu.base.StuImpl;

public class Stu_Java_ClassLoader extends StuImpl {
    @Override
    public void a_是什么() {
        // 类加载器
        /*
            ClassLoader 是一个抽象类，定义了ClassLoader的主要功能

            // 根加载器
            BootstrapClassLoader 负责加载 JVM 运行时核心类

            // 扩展类加载器
            ExtensionClassLoader 负责加载 JVM 扩展类

            // 应用加载器
            AppClassLoader 才是直接面向我们用户的加载器，它会加载 Classpath 环境变量里定义的路径中的 jar 包和目录。
                我们自己编写的代码以及使用的第三方 jar 包通常都是由它来加载的

            ExtensionClassLoader 和 AppClassLoader 都是 URLClassLoader 的子类
         */
    }

    @Override
    public void s_面试点() {
        // Q：class.forName() 与 classLoader.loadClass() 的区别？
        // A：class.forName() = classLoader.loadClass() + 类初始化

    }

    @Override
    public void b_作用() {
        //
        stu_loadClass();

        //
        stu_findClass();

        //
        stu_defineClass();
    }

    private void stu_loadClass() {

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
}
