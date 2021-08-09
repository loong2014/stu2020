package com.sunny.module.stu.F类加载器ClassLoader.java;

import java.lang.reflect.InvocationTargetException;

public class MainC {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //BootstrapClassLoader 负责加载 JVM 运行时核心类

        //ExtensionClassLoader 负责加载 JVM 扩展类

        //AppClassLoader 才是直接面向我们用户的加载器，它会加载 Classpath 环境变量里定义的路径中的 jar 包和目录。
        // 我们自己编写的代码以及使用的第三方 jar 包通常都是由它来加载的

        //ExtensionClassLoader 和 AppClassLoader 都是 URLClassLoader 的子类


        //类加载流程： 加载---链接(验证，准备，解析)---初始化
        // 加载：字节码数据读取到JVM中，映射为JVM认可的数据结构
        // 链接：连接是把原始的类定义信息平滑地转入JVM运行的过程中。这一阶段可以细分为验证、准备、解析三步。
        // 初始化：初始化是执行类初始化的代码逻辑，包括静态字段赋值的动作，以及执行类定义中的静态初始化块内的逻辑。

//        test1();
//        test2();

        CSunny sunny = new CSunny();
        Object object = new Object();

        Integer i;
        Float f;
        Double d;
        Long l;
        Short s;
        Character c;
        Byte b;
        Boolean bo
                ;
    }

    public static void test1() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //自定义类加载
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                System.out.println("自定义类加载");
                return super.loadClass(name);
            }

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                return super.findClass(name);
            }
        };


        System.out.println("000 name :" + classLoader);

        ClassLoader loader = classLoader.getParent();
        System.out.println("111 name :" + loader); //AppClassLoader
        if (loader != null) {
            loader = loader.getParent();
            System.out.println("222 name :" + loader); //ExtClassLoader
        }
        if (loader != null) {
            loader = loader.getParent();
            System.out.println("333 name :" + loader); // null 也就是 BootClassLoader
        }


        Class clazz = classLoader.loadClass("com.sunny.module.stu.F类加载器ClassLoader.java.CSunny");
        Object object = clazz.getConstructor().newInstance();
        System.out.println("object :" + object);

    }

    public static void test2() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        //
        System.out.println("\n 类加载的区别");
        String filePath = "com.sunny.module.stu.类加载C.CSunny";

        System.out.println("\n 触发类的直接引用");
        // 类的直接引用会触发初始化
        // 1. new CSunny() //通过实例话
//        CSunny sunny = new CSunny();
        // 2. CSunny.Index //使用静态变量，调用静态方法
//        int index = CSunny.Index;
        // 3. 反射
//        Class.forName(filePath);
        // 4. 初始化子类，触发父类初始化
//        new CSunnySon();


        // Class.forName 与 ClassLoader.loadClass 的区别


        System.out.println("类加载");
        ClassLoader classLoader1 = ClassLoader.getSystemClassLoader();
        Class loaderClazz = classLoader1.loadClass(filePath); // 加载+链接

        loaderClazz.newInstance();// 初始化+实例化 // index =11
        System.out.println("loaderClazz :" + loaderClazz.getName());

        System.out.println("反射加载");
        Class forNameClazz = Class.forName(filePath); // 类加载+初始化
//        Class forNameClazz = Class.forName(filePath, false, classLoader1); // 指定不执行初始化
        System.out.println("forNameClazz :" + forNameClazz);
        forNameClazz.newInstance();// 实例化，因为初始化以及执行过，这里只执行了实例化 //index =12

    }
}
