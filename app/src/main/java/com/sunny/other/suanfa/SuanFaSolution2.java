package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFaSolution2 {


    public static void main(String[] args) {

        int put = 12345;
        int out = reverse(put);
        System.out.println(put + " >>> " + out);
    }

    private static int reverse(int x) {

        String put = Integer.toString(x);

        char[] chars = put.toCharArray();

        System.out.println(chars + "111 >>> " + chars);


        char tmp;
        int len = chars.length;
        for (int l = 0, r = len - 1; l < r; l++, r--) {
            tmp = chars[l];
            chars[l] = chars[r];
            chars[r] = tmp;
        }

        System.out.println(chars + "222 >>> " + chars);

        String out = chars.toString();
        System.out.println(put + " >>> " + out);


        return Integer.parseInt(out);
    }

}
