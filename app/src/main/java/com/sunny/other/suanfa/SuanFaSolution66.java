package com.sunny.other.suanfa;

import com.sunny.other.SuanFaTools;

/**
 * Created by zhangxin17 on 2020-04-24
 * 66. 加一
 */
public class SuanFaSolution66 {


    public static void main(String[] args) {

        int[] nums = new int[]{9, 4, 9};
        System.out.println("nums :" + SuanFaTools.toString(nums));
        int[] out = plusOne3(nums);
        System.out.println("out :" + SuanFaTools.toString(out));
    }

    public static int[] plusOne(int[] digits) {


        digits = new int[digits.length + 2];
        System.out.println("plusOne :" + SuanFaTools.toString(digits));

        return digits;
    }

    public static int[] plusOne3(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) return digits;
        }
        // 这个处理，绝妙
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    public static int[] plusOne2(int[] digits) {
        int len = digits.length;
        for (int i = len - 1; i >= 0; i--) {
            if (digits[i] != 9) {
                digits[i] = digits[i] + 1;
                return digits;
            }
            digits[i] = 0;
        }

        int[] out = new int[len + 1];
        out[0] = 1;
        return out;
    }


}
