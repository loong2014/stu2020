package com.sunny.module.stu.B反射.序列化A;

import java.io.Serializable;

public class BASunny implements Serializable {

    private String name;
    private int age;
    private float money;

    public BASunny(String name, int age, float money) {
        this.name = name;
        this.age = age;
        this.money = money;
    }

    @Override
    public String toString() {
        return "BASunny{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                '}';
    }
}
