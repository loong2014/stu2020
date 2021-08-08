package com.sunny.module.stu.AJAVA;

import java.util.Objects;

public class BaseSunny {
    private String name;
    private int age;

    public BaseSunny(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    public String getName() {
        return this.name;
    }

    public String getTip(String tip) {
        return "this name is " + name + " , " + tip;
    }
}
