package com.sunny.other;

/**
 * Created by zhangxin17 on 2020-05-06
 */
public class JavaString {

    public static void main(String[] args) {

        String str = new String("abc");
        str = str.intern();

        String numStr = "01";
        int num = Integer.parseInt(numStr); // 1
        System.out.println("num :" + num);
    }
}
