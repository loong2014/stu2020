package com.sunny.other.suanfa;

/**
 * 263. 丑数
 * 编写一个程序判断给定的数是否为丑数。
 * <p>
 * 丑数就是只包含质因数 2, 3, 5 的正整数。
 * <p>
 * 习惯上我们把1当做是第一个丑数。
 */
public class SuanFaSolution263 {

    public static void main(String[] args) {
        System.out.println("out :" + isUgly(1));
    }

    public static boolean isUgly(int num) {
        if (num <= 0) {
            return false;
        }
        int[] factor = new int[]{2, 3, 5};
        for (int i : factor) {
            while (num % i == 0) {
                num = num / i;
            }
        }

        return num == 1;
    }
}
