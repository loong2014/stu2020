package com.sunny.family;

import com.google.gson.Gson;

public class DemoJava {

    interface Function<Arg, Return> {
        Return apply(Arg arg);
    }

    Function<String, Integer> strLen = new Function<String, Integer>() {
        @Override
        public Integer apply(String s) {
            return s.length();
        }
    };

    interface PhotoCallback {
        void getList();
    }

    private PhotoCallback callback = new PhotoCallback() {
        @Override
        public void getList() {

        }
    };

    public static void main(String[] args) {
        DemoJava demoJava = new DemoJava();

        int len = demoJava.strLen.apply("hello");
        System.out.println("len of hello :" + len);

    }
}
