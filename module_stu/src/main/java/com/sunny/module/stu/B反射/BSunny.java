package com.sunny.module.stu.B反射;

public class BSunny {

    public static int num = 0;
    private String name;

    private int age;

    public BSunny() {
        num = 10;
    }

    public BSunny(String name) {
        this.name = name;
        num = 20;
    }

    private BSunny(int age) {
        this.age = age;
        num = 30;
    }

    public BSunny(String name, int age) {
        this.name = name;
        this.age = age;
        num = 40;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("我的名字 :" + name);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void showTip(String tip) {
        System.out.println("只有努力之后，才知道可不可以 :" + tip);
    }

    public void buildName(BSunny2 sunny2, String tip) {
        String nameTip = sunny2.buildTip(tip);
        System.out.println("buildName :" + nameTip);
    }

    @Override
    public String toString() {
        return "BSunny{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", num=" + num +
                '}';
    }
}
