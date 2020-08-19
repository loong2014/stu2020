package com.sunny.other;

/**
 * Created by zhangxin17 on 2020/6/19
 */
public class SuanFaTools {

    public static String toString(Object[] array) {
        if (array == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : array) {
            sb.append(obj);
        }
        return sb.toString();
    }


    public static String toString(int[][] array) {
        if (array == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int[] i : array) {
            sb.append("\n");
            for (int j : i) {
                sb.append(j + " ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String toString(int[] array) {
        if (array == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i);
        }
        return sb.toString();
    }


    public static String toString(char[] array) {
        if (array == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char i : array) {
            sb.append("" + i);
        }
        return sb.toString();
    }
}
