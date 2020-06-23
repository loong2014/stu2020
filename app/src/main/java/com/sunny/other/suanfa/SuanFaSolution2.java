package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFaSolution2 {


    public static void main(String[] args) {

        int put = -12345600;
        int out = reverse(put);
        System.out.println(put + " >>> " + out);
    }

    private static int reverse(int x) {
        int ans = 0;
        while (x != 0) {
            if ((ans * 10) / 10 != ans) {
                ans = 0;
                break;
            }
            ans = ans * 10 + x % 10;
            x = x / 10;
        }
        return ans;
    }

    private static int reverse2(int x) {

        boolean fu = x < 0;
        if (fu) {
            x = -x;
        }

        String put = Integer.toString(x);

        char[] chars = put.toCharArray();

        char tmp;
        int len = chars.length;
        for (int l = 0, r = len - 1; l < r; l++, r--) {
            tmp = chars[l];
            chars[l] = chars[r];
            chars[r] = tmp;
        }

        String outStr = new String(chars);


        int out = 0;
        try {
            out = Integer.parseInt(outStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        if (fu) {
            return -out;
        }
        return out;
    }

}
