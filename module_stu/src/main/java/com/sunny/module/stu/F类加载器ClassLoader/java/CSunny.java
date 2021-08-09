package com.sunny.module.stu.F类加载器ClassLoader.java;

public class CSunny {

    public static int Index = 10;

    public static final String SName = "final name";

    {
        System.out.println("normal block");
    }

    static {
        System.out.println("static block :" + Index++);
    }

    private String name;
    private int age;


    public CSunny() {
        System.out.println("Construction" + Index++);

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "CSunny{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
