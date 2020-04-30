package com.sunny.other;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangxin17 on 2020-04-22
 */
public class JavaDemo2 {

    private void listDemo() {
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("Ddd");
        list.add("eee");


        // 不能进行删除操作
//        for (String str : list) {
//            if ("aaa".equals(str)) {
//                list.remove(str);
//            }
//            System.out.println("out " + str);
//        }

        // 可以进行删除操作
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String str = iterator.next();
            if ("Ddd".equals(str)) {
                iterator.remove();
            }

            if ("bbb".equals(str)) {
                System.out.println("out " + str);
            }
        }

    }

    private void strDemo() {
        String str1 = "abc";
        String str2 = new String("abc");
        str2 = "a" + "b" + "c";

        System.out.println(str1 == str2);
        System.out.println(str1.equals(str2));
    }

    public static void main(String[] args) {

        JavaDemo2 demo = new JavaDemo2();
        demo.strDemo();
    }
}
