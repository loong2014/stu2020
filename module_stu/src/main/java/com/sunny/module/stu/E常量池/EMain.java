package com.sunny.module.stu.E常量池;

public class EMain {
    public static void main(String[] args) {
        String str = new String("abc");

        //
        int i1 = 100; //-128 ~ 127 ，直接从缓存中返回，不创建新的对象
        Integer i2 = Integer.valueOf(10); // 同i1
        Integer i3 = new Integer(20); //通过new 必创建新对象

        // 通过xxx.valueOf()，判断是否有缓存
        //
        float f1 = 10F;// 无缓存
        Float f2 = Float.valueOf(10); // 无缓存

        //
        short s1 =10;
        Short s2 =Short.valueOf(s1); // -128 ~ 127

        //
        double d1 = 10D; // 无缓存
        Double d2 = Double.valueOf(10); // 无缓存

        //
        long l1 =10;
        Long l2 = 10L;
        Long l3 = Long.valueOf(l1); // -128 ~ 127

        //
        boolean b1  =false;
        Boolean b2 =   Boolean.valueOf(false);
        Boolean b3 = new Boolean(b1);
        Boolean b4 = new Boolean("true");

        //
        char c1 = 'S';
        char c2 = 12;
        Character c3 = Character.valueOf(c1); // 0 ~ 127
        Character c4 = new Character('S');

        //
        byte bt1 =10;
        Byte bt2 = 10;
        Byte bt3 = Byte.valueOf(bt1); // -128 ~ 127
        Byte bt4 = new Byte(bt1);
        Byte bt5 = new Byte("c");
        //
        System.out.println("str :" + str);
    }
}
