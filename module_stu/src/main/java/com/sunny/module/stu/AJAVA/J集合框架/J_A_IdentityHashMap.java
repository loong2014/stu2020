package com.sunny.module.stu.AJAVA.J集合框架;

import com.sunny.module.stu.AJAVA.BaseSunny;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class J_A_IdentityHashMap {


    private static void textSunny() {

//        Map<BaseSunny, Integer> map = new IdentityHashMap<>();
        Map<BaseSunny, Integer> map = new HashMap<>();


        BaseSunny sunny1 = new BaseSunny("abc");
        BaseSunny sunny2 = new BaseSunny("abc");
        BaseSunny sunny3 = new BaseSunny("abc");

        map.put(sunny1, 1);
        map.put(sunny2, 2);
        map.put(sunny3, 3);

        System.out.println("" + map.size());
        System.out.println("sunny1 :" + map.get(sunny1));
    }

    private static void textString() {

        IdentityHashMap<String, Integer> map = new IdentityHashMap<>();
//        HashMap<String, Integer> map = new HashMap<>();


        String sunny1 = new String("abc");
        String sunny2 = new String("abc");
        String sunny3 = new String("abc");

        map.put(sunny1, 10);
        map.put(sunny2, 20);
        map.put(sunny3, 30);

        // String 重写了hashCode
        int h1 = sunny1.hashCode();
        int h2 = sunny2.hashCode();
        int h3 = sunny3.hashCode();
        // hashCode :96354 , 96354 , 96354
        System.out.println("hashCode :" + h1 + " , " + h2 + " , " + h3);


        Object o;
        //
        h1 = System.identityHashCode(sunny1);
        h2 = System.identityHashCode(sunny2);
        h3 = System.identityHashCode(sunny3);
        // identityHashCode :2016447921 , 666988784 , 1414644648
        System.out.println("identityHashCode :" + h1 + " , " + h2 + " , " + h3);


        // HashMap ===== 1
        // IdentityHashMap ===== 3
        System.out.println("" + map.size());


        // HashMap ===== 30
        // IdentityHashMap ===== 10
        System.out.println("sunny1 :" + map.get(sunny1));
    }

    public static void main(String[] args) {
        textString();

    }
}
