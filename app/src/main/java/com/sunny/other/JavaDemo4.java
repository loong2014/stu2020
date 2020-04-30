package com.sunny.other;

/**
 * Created by zhangxin17 on 2020-04-22
 */
public class JavaDemo4 {

    private static class SingletonHolder {
        private static final JavaDemo4 instalce = new JavaDemo4();
    }

    private JavaDemo4() {

    }

    public static JavaDemo4 getInstance() {
        return SingletonHolder.instalce;
    }
}
