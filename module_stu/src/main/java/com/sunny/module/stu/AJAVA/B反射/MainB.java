package com.sunny.module.stu.AJAVA.B反射;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainB {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        String name = "com.sunny.module.stu.反射B.BSunny";

        //
        BSunny sunny = new BSunny();
        Class clazz1 = sunny.getClass();
        System.out.println("111 class :" + clazz1);

        //
        Class clazz2 = Class.forName(name);
        System.out.println("222 class :" + clazz2);

        //
        Class clazz3 = BSunny.class;
        System.out.println("333 class :" + clazz3);

        //
        System.out.println("clazz1 == clazz2 :" + (clazz1 == clazz2)); // true
        System.out.println("clazz1 == clazz3 :" + (clazz1 == clazz3)); // true
        System.out.println("clazz2 == clazz3 :" + (clazz2 == clazz3)); // true

        // 以上可以得出，在运行期间，一个类只有一个class对象

        Class clazz = clazz2;
        Object object;


        ///////////// 通过反射获取构造函数

        /**
         <-- 1. 通过Constructor 类对象获取类构造函数信息 -->
         String getName()；// 获取构造器名
         Class getDeclaringClass()；// 获取一个用于描述类中定义的构造器的Class对象
         int getModifiers()；// 返回整型数值，用不同的位开关描述访问修饰符的使用状况
         Class[] getExceptionTypes()；// 获取描述方法抛出的异常类型的Class对象数组
         Class[] getParameterTypes()；// 获取一个用于描述参数类型的Class对象数组
         */
        Constructor[] conArray;
//        conArray = clazz.getConstructors(); // 公有
        conArray = clazz.getDeclaredConstructors(); // 所有（公有，私有，默认，受保护）

        System.out.println("\n构造方法");
        for (Constructor c : conArray) {
            System.out.println("111 :" + c);
            System.out.println("222 :" + c.getDeclaringClass());
        }

        //获取单个的方法
        System.out.println("\n");
        Constructor con;

//        con = clazz.getConstructor(); //默认
        con = clazz.getConstructor(String.class); //公有带参
//        con = clazz.getDeclaredConstructor(String.class); //带参私有
        System.out.println(con);

        // 公有带参
        con = clazz.getConstructor(String.class);
        Object object1 = con.newInstance("张三"); // 通过 newInstance 调用构造方法
        System.out.println("public object :" + object1); // num = 20

        //私有带参
        con = clazz.getDeclaredConstructor(int.class);
        con.setAccessible(true);//暴力访问(忽略掉访问修饰符)
        Object object2 = con.newInstance(18);
        System.out.println("private object :" + object2);//num = 30


        //静态参数是全局的，对在object2中对num的修改，会同步到object1中
        System.out.println("public object :" + object1); // num = 30 ，而不是20


        ////////////////成员变量
        object = clazz.getConstructor().newInstance();
        System.out.println("\n成员变量");
        System.out.println("object :" + object);

        /**
         <-- 2. 通过Field类对象获取类属性信息 -->
         String getName()；// 返回属性的名称
         Class getDeclaringClass()； // 获取属性类型的Class类型对象
         Class getType()；// 获取属性类型的Class类型对象
         int getModifiers()； // 返回整型数值，用不同的位开关描述访问修饰符的使用状况
         Object get(Object obj) ；// 返回指定对象上 此属性的值
         void set(Object obj, Object value) // 设置 指定对象上此属性的值为value
         */
        Field[] fieldArray;
        fieldArray = clazz.getFields(); // 公有
        fieldArray = clazz.getDeclaredFields(); //所有（公有，私有，默认，受保护）
        for (Field f : fieldArray) {
            System.out.println(f);
        }

        //
        System.out.println("\n修改字段 num");
        Field f = clazz.getField("num");
        f.set(object, 80);
        System.out.println("object :" + object);

        //
        Field f2 = clazz.getDeclaredField("name");
        f2.setAccessible(true);//暴力反射，解除私有限定
        f2.set(object, "李四");
        System.out.println("object :" + object);


        ////////////////方法
        object = clazz.getConstructor().newInstance();
        System.out.println("\n成员方法");
        System.out.println("object :" + object);

        /**
         <-- 3. 通过Method 类对象获取类方法信息 -->
         String getName()；// 获取方法名
         Class getDeclaringClass()；// 获取方法的Class对象
         int getModifiers()；// 返回整型数值，用不同的位开关描述访问修饰符的使用状况
         Class[] getExceptionTypes()；// 获取用于描述方法抛出的异常类型的Class对象数组
         Class[] getParameterTypes()；// 获取一个用于描述参数类型的Class对象数组
         */
        Method[] methodArray;
        methodArray = clazz.getMethods(); //所有公有方法，包括父类的
        methodArray = clazz.getDeclaredMethods(); //+私有
        for (Method m : methodArray) {
            System.out.println(m);
//            System.out.println(m.getName());
        }

        //
        Method method = clazz.getMethod("setName", String.class);
        method.invoke(object, "晴天");
        System.out.println("object :" + object);

        BSunny2 sunny2 = new BSunny2();

        method = clazz.getDeclaredMethod("buildName", BSunny2.class, String.class);
        method.setAccessible(true);//暴力反射，解除私有限定
        method.invoke(object, sunny2, "加油");

        //
        System.out.println("\n接口");
        Class iClazz = IBSunny.class;
        methodArray = iClazz.getDeclaredMethods(); //+私有
        for (Method m : methodArray) {
            System.out.println(m);
        }


//        Object iObject = iClazz.getConstructor().newInstance(); // error，接口没有构造方法
//        method = iClazz.getDeclaredMethod("showTip", String.class);
//        method.setAccessible(true);//暴力反射，解除私有限定
//        method.invoke(object, "加油");

    }
}
