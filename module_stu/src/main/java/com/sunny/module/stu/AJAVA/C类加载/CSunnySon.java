package com.sunny.module.stu.AJAVA.C类加载;

public class CSunnySon extends CBase {

    public static final String SName = "CSunnySon";

    {
        System.out.println("类代码块  CSunnySon");
    }

    static {
        System.out.println("静态代码块  CSunnySon :" + SName);
        System.out.println("静态代码块  CSunnySon  baseIndex :" + sBaseIndex);
    }

    public CSunnySon() {
        sBaseIndex++;
        System.out.println("构造方法  CBase  sBaseIndex :" + sBaseIndex);
    }

}
