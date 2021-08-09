package com.sunny.module.stu.F类加载器ClassLoader.java;

public class CBase {

    public static int sBaseIndex = 100;

    public static final String SName = "CBase";

    {
        System.out.println("类代码块  CBase");
    }

    static {
        System.out.println("静态代码块  CBase :" + SName);
        System.out.println("静态代码块  CBase  baseIndex :" + sBaseIndex);
    }

    private String name;

    private int age;


    public CBase() {
        sBaseIndex++;
        System.out.println("构造方法  CBase  sBaseIndex :" + sBaseIndex);
    }

    public String getInfo() {
        return buildInfo();
    }

    private String buildInfo() {
        return "CBase_" + sBaseIndex + ":" + name + "_" + age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        int i = 0;
        try {
            Object o = name;
            i = 1;
        } catch (Exception e) {
            i = 2;
            e.printStackTrace();
        } finally {
        }

        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
