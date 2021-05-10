package com.sunny.lib.common.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SunTimeUtils {
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat ymdFormatter = new SimpleDateFormat("yy-MM-dd HH-mm-ss", Locale.getDefault());

    public static long getCurTime() {
        return System.currentTimeMillis();
    }

    public static String getCurTimeStr() {
        return ymdFormatter.format(getCurTime());
    }
}
