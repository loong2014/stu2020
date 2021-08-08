package com.sunny.module.stu.AJAVA.FString;

import com.sunny.module.stu.base.StuImpl;

public class 总结String extends StuImpl {
    @Override
    public void a_是什么() {
        // final 类型的类
        // 可以直接通过 = 进行赋值
        String str = "abc";
        String str2 = new String("abc");
        String str3 = String.valueOf("abc");
        byte[] bytes = {'a', 'b', 'c'};
        String str4 = String.valueOf(bytes);
    }

    @Override
    public void s_数据结构() {
        // char[] 数组
    }

    @Override
    public void b_作用() {
        // 字符串常量池

        String s = "abc";
        /*
            1、先去【字符串常量池】中查找是否存在字符串"abc"，如果存在返回其引用
            2、如果不存在，则创建"abc"实例，并放入【字符串常量池】中，并返回其引用
         */

        String s1 = "a" + "b" + "c"; // == s
        /*
            在编译期，会将 "a" , "b" , "c" 合并到一起作为"abc"存在，去常量池中查找
         */

        String s2 = new String("abc"); // != s
        /*
            在【堆】中创建一个String实例，"abc"数据存放在，并返回其引用
         */

        String s3 = String.valueOf("abc"); // == s
        /*
            从常量池中查找"abc"
         */
    }


    /**
     * 线程不安全
     */
    private String stuStringBuilder() {
        /*
        中间字符串不会添加到常量池
        内存通过char[] 存储数据，可自动扩容
         */
        StringBuilder sb = new StringBuilder();
        sb.append(18);
        sb.append("张三");
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        return sb.toString();
    }

    /**
     * 线程安全
     * 通过给append方法添加 synchronized 关键字
     */
    private String stuStringBuffer() {
        StringBuffer sb = new StringBuffer();
        sb.append(18);
        sb.append("张三");
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }

        return sb.toString();
    }

    @Override
    public void s_面试点() {
        String s1 = "abc";

        String s2 = "a" + "b" + "c"; // true
        String s3 = new String("abc"); // false
        String s4 = String.valueOf("abc"); // true
        String s5 = String.valueOf("abc").intern(); // true
        byte[] bytes = {'a', 'b', 'c'};
        String s6 = String.valueOf(bytes);

        System.out.println("s1 == s2 :" + (s1 == s2)); // true
        System.out.println("s1 == s3 :" + (s1 == s3)); // false
        System.out.println("s1 == s4 :" + (s1 == s4)); // true
        System.out.println("s1 == s5 :" + (s1 == s5)); // true
        System.out.println("s1 == s6 :" + (s1 == s6)); // false
    }

    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "a" + "b" + "c";
        String s3 = new String("abc");
        String s4 = String.valueOf("abc");
        String s5 = String.valueOf("abc").intern();
        byte[] bytes = {'a', 'b', 'c'};
        String s6 = String.valueOf(bytes);

        System.out.println("s1 == s2 :" + (s1 == s2)); // true
        System.out.println("s1 == s3 :" + (s1 == s3)); // false
        System.out.println("s1 == s4 :" + (s1 == s4)); // true
        System.out.println("s1 == s5 :" + (s1 == s5)); // true
        System.out.println("s1 == s6 :" + (s1 == s6)); // true
    }

}
