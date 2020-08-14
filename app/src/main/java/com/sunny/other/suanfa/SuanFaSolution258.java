package com.sunny.other.suanfa;

/**
 * 258. 各位相加
 * 给定一个非负整数 num，反复将各个位上的数字相加，直到结果为一位数。
 */
public class SuanFaSolution258 {

    public static void main(String[] args) {
        System.out.println("out :" + addDigits(38));
    }

    /**
     * 除个位外，每一位上的值都是通过(9+1)进位的过程得到的
     */
    public static int addDigits(int num) {
        return (num - 1) % 9 + 1;
    }

    /**
     * 循环
     */
    public static int addDigits3(int num) {
        int next = num;
        while (num > 9) {
            next = 0;
            while (num > 0) {
                next = next + num % 10;
                num = num / 10;
            }

            num = next;
        }
        return next;
    }

    /**
     * 递归
     */
    public static int addDigits2(int num) {
        if (num < 10) {
            return num;
        }
        int next = 0;
        while (num > 0) {
            next = next + num % 10;
            num = num / 10;
        }

        return addDigits(next);
    }

}
