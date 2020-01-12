package com.sunny.family;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class Demo {


    /**
     * 关闭某一类型的Activity
     */
    public static void finishActivity(Class c) {
        List<Activity> list = new ArrayList<>();

        for (int i = list.size() - 1; i >= 0; i--) {
            Activity activity = list.get(i);
            if (activity != null && activity.getClass().isAssignableFrom(c)) {
                activity.finish();
            }
        }
    }
}
