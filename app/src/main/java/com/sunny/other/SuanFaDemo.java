package com.sunny.other;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by zhangxin17 on 2020-04-22
 */
public class SuanFaDemo {



    private static SuanFaDemo sInstance = null;

    /**
     * 双重检查
     */
    public static SuanFaDemo getInstance() {
        if (sInstance == null) {
            synchronized (SuanFaDemo.class) {
                if (sInstance == null) {
                    sInstance = new SuanFaDemo();
                }
            }
        }
        return sInstance;
    }

    /**
     * 静态内部类
     */
    private static class SingletonHolder {
        private static final SuanFaDemo instance = new SuanFaDemo();
    }

    public static SuanFaDemo getInstance2() {
        return SingletonHolder.instance;
    }

    private SuanFaDemo() {

    }


    /**
     * 常见的单例模式有那儿些？你经常用哪儿种方式？手写一下？
     */
    private void algor3() {

    }

    /**
     * 1.这段代码的输出结果是什么？
     * 2.for循环里最后一个表达式可以改成i++么？
     * 3.这里为什么使用LinkedHashSet，而不是ArrayList？
     */
    private void algor1() {
        Set<String> itemsResult = new LinkedHashSet<>();

        for (int i = 0; i < 50; i = itemsResult.size()) {

            int a = (int) (Math.random() * 100);
            int b = (int) (Math.random() * 100);
            if ((a + b) < 100 && a > 10 && b > 10) {
                String str;
                String result;

                if ((a + b) % 2 == 0) {
                    str = a + " + " + b + " = ";
                    result = str + (a + b);
                } else {
                    str = Math.max(a, b) + " - " + Math.min(a, b) + " = ";
                    result = str + Math.abs(a - b);
                }

                itemsResult.add(result);
            }
        }

        for (String item : itemsResult) {
            System.out.println(item);
        }

    }

    public static void mai1n(String[] args) {
        Set<String> itemsResult = new LinkedHashSet<>();

        for (int i = 0; i < 50; i = itemsResult.size()) {

            int a = (int) (Math.random() * 100);
            int b = (int) (Math.random() * 100);
            if ((a + b) < 100 && a > 10 && b > 10) {
                String str;
                String result;

                if ((a + b) % 2 == 0) {
                    str = a + " + " + b + " = ";
                    result = str + (a + b);
                } else {
                    str = Math.max(a, b) + " - " + Math.min(a, b) + " = ";
                    result = str + Math.abs(a - b);
                }

                if (!itemsResult.contains(result)) {
                    itemsResult.add(result);
                }
            }
        }

        for (String item : itemsResult) {
            System.out.println(item);
        }
    }


    static class A {

        public A() {
            System.out.println("1111");
        }

        public void age() {
            System.out.println("2222");

        }
    }

    static class B extends A {
        public B() {
            System.out.println("3333");
        }

        @Override
        public void age() {
            super.age();
            System.out.println("4444");

        }
    }

    static class C extends B{
        @Override
        public void age() {
            super.age();
            System.out.println("55555");
        }
    }

    public static void main(String[] args) {
//        SuanFaDemo demo = new SuanFaDemo();
//        demo.algor1();
//        demo.algor2();
//        demo.generateKH(4);

        A a = new C();
        a.age();

    }

    private List<String> generateKH(int num) {


        Stack<Boolean> stack1 = new Stack<>();

        Stack<Boolean> stack2 = new Stack<>();

        stack1.push(true);
        stack1.push(false);
        for (int i = 1; i < num; i++) {
            stack2.push(stack1.pop());
            stack1.push(true);
            stack1.push(false);
            stack1.push(stack2.pop());
        }


        List<String> list = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        while (!stack1.empty()) {
            if (stack1.pop()) {
                builder.append("{");
//                list.add("{");
            } else {
                builder.append("}");
//                list.add("}");
            }
        }

        System.out.println(builder.toString());


        return list;
    }

}
