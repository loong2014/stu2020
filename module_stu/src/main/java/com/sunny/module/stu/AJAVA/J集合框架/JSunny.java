package com.sunny.module.stu.AJAVA.J集合框架;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

public class JSunny {
    public static void main(String[] args) {
        Map<Object, Boolean> map = new IdentityHashMap<>();

        map.put(new String("abc"), true);
        map.put(new String("abc"), true);
        map.put(new String("abc"), true);
        map.put(new String("abc"), true);

        System.out.println("" + map.size());


        Map<Object, Boolean> syncMap = Collections.synchronizedMap(map);
    }
}
