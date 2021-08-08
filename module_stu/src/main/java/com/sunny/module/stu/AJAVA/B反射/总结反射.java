package com.sunny.module.stu.AJAVA.B反射;

import com.sunny.module.stu.base.StuImpl;

public class 总结反射 extends StuImpl {

    @Override
    public void a_是什么() {
        // 在运行期，动态获取/修改对象信息
    }

    @Override
    public void b_作用() {
        // 使代码更加灵活
        // 动态加载类对象
        // 可以在程序运行时对类对象进行操作，字段信息，方法信息，构造方法信息
    }

    @Override
    public void w_为什么用() {
        // 可以在运行时，根据运行情况，动态加载类。避免加载一些不会被使用到的类
    }

    @Override
    public void q_缺点() {
        // 破坏了类的【封装】特性
        // 导致类的属性安全问题
        // 动态加载会存在一些性能问题
    }

    @Override
    public void d_怎么用() {

        /*
        获取构造方法/参数/类方法
            getXxx // 公有
            getDeclaredXxx // 全部

        启用和禁用访问安全检查的开关，值为 true，则表示反射的对象在使用时应该取消 java 语言的访问检查；反之不取消
        setAccessible(true)

        获取实例
            constructor.newInstance()
        修改属性
            filed.set(object, param)
        调用方法
            method.invoke(object, param)
         */


        获取构造方法();

        生成实例();

        获取属性();

        获取类方法();
    }

    private void 获取构造方法() {

        // 构造方法 Constructor
        /*
         <-- 1. 通过Constructor 类对象获取类构造函数信息 -->
         String getName()；// 获取构造器名
         Class getDeclaringClass()；// 获取一个用于描述类中定义的构造器的Class对象
         int getModifiers()；// 返回整型数值，用不同的位开关描述访问修饰符的使用状况
         Class[] getExceptionTypes()；// 获取描述方法抛出的异常类型的Class对象数组
         Class[] getParameterTypes()；// 获取一个用于描述参数类型的Class对象数组
         */

        // 获取所有 Constructor[] conArray =
        // clazz.getConstructors(); // 公有
        // clazz.getDeclaredConstructors(); // 所有（公有，私有，默认，受保护）

        // 获取单个 Constructor con =
        // clazz.getConstructor(); //默认
        // clazz.getConstructor(String.class); //公有带参
        // clazz.getDeclaredConstructor(String.class); //带参私有
    }

    private void 生成实例() {
        // 根据 Constructor con 生成实例对象
        // 公有
        // Object obj = con.newInstance("张三");

        // 私有
        // con.setAccessible(true);
        // Object obj = con.newInstance("张三");
    }

    private void 获取属性() {
        // 变量 Field
        /*
         <-- 2. 通过Field类对象获取类属性信息 -->
         String getName()；// 返回属性的名称
         Class getDeclaringClass()； // 获取属性类型的Class类型对象
         Class getType()；// 获取属性类型的Class类型对象
         int getModifiers()； // 返回整型数值，用不同的位开关描述访问修饰符的使用状况
         Object get(Object obj) ；// 返回指定对象上 此属性的值
         void set(Object obj, Object value) // 设置 指定对象上此属性的值为value
         */

        // 获取所有 Field[] fieldArray
        // fieldArray = clazz.getFields(); // 公有
        // fieldArray = clazz.getDeclaredFields(); //所有（公有，私有，默认，受保护）

        // 获取单个 Field f =
        // clazz.getField("num");
        // clazz.getDeclaredField("name")

        // 修改变量
        // 公有
        // f.set(object, 80)

        // 私有
        // f.setAccessible(true);
        // f.set(object, "李四");
    }

    private void 获取类方法() {

        // 方法 Method
        /*
         <-- 3. 通过Method 类对象获取类方法信息 -->
         String getName()；// 获取方法名
         Class getDeclaringClass()；// 获取方法的Class对象
         int getModifiers()；// 返回整型数值，用不同的位开关描述访问修饰符的使用状况
         Class[] getExceptionTypes()；// 获取用于描述方法抛出的异常类型的Class对象数组
         Class[] getParameterTypes()；// 获取一个用于描述参数类型的Class对象数组
         */

        // 获取所有 Method[] methodArray
        // methodArray = clazz.getMethods();
        // methodArray = clazz.getDeclaredMethods();


        // 获取单个 Method method =
        // clazz.getMethod("setName", String.class);
        // clazz.getDeclaredMethod("buildName", BSunny2.class, String.class);

        // 调用方法
        // 公有
        // method.invoke(object, "晴天");

        // 私有
        // f.setAccessible(true);
        // method.invoke(object, sunny2, "加油");
    }
}
