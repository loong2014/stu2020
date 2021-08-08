package com.sunny.module.stu.AJAVA.FString;

public class FMain {
    public static void main(String[] args) {
        int age = 18;
        String str = new String("abc" + age); // abc18
        System.out.println("111 :"+str);

        //
        char[] chars = {'a','b','c'};
        str = new String(chars);
        System.out.println("222 :"+str); // abc

        //
        str = new String(chars,1,1);
        System.out.println("333 :"+str); // bc

        //
        int[] ints = {1,2,3,4,5};
        str = new String(ints,1,2);
        System.out.println("444 :"+str);

    }
}
