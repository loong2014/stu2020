package com.sunny.other;

import java.util.List;

/**
 * Created by zhangxin17 on 2020-04-22
 */
public class JavaDemo {

    public static class A {

        public A() {
            System.out.println("1111");
        }

        protected List<String> print() {
            System.out.println("2222");
            return null;
        }
    }

    public static class B extends A {

        public B() {
            super();
            System.out.println("3333");
        }

        public B(String tag) {
            System.out.println("4444");
        }

        @Override
        public List<String> print() {
            System.out.println("5555");
            super.print();
            return null;
        }

    }

    /**
     * 输出结果顺序是什么？
     * 1111 -> 4444 -> 5555 -> 2222
     * <p>
     * 将B中print的返回类型改为ArryList，问会不会异常？
     * Java7之后返回值类型可以是父类返回值的派生类
     */
    public static void main(String[] args) {
        A a = new B("hi");
        a.print();
    }
}
