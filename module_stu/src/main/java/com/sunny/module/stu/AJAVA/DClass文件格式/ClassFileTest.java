package com.sunny.module.stu.AJAVA.DClass文件格式;

import java.util.HashMap;
import java.util.Map;

public class ClassFileTest {
    public String mString = "blog.csdn.net/tjiyu";
    private static String mStaticString = "hello";
    private final static String mFinalStaticString = "java";
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(mStaticString, mFinalStaticString);
        return map;
    }

}