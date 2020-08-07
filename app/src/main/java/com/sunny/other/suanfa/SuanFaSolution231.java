package com.sunny.other.suanfa;

/**
 * 231. 2的幂
 * 给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
 */
public class SuanFaSolution231 {

    public static void main(String[] args) {
        System.out.println("out :" + isPowerOfTwo(5));
    }

    /**
     * 位运算，补码
     */
    public static boolean isPowerOfTwo(int n) {
        if (n == 0) {
            return false;
        }
        long x = n;
        // 去除二进制中最右边的 1
//        return (x & (x - 1)) == 0;

        // 获取最右边的 1：
        return (x & (-x)) == x;
    }

    /**
     * 位运算
     */
    public static boolean isPowerOfTwo2(int n) {
        if (n < 1) {
            return false;
        }

        if (n == 1) {
            return true;
        }

        while (n > 0) {
            if (n == 1) {
                return true;
            }

            if ((n & 1) == 1) {
                return false;
            }
            n = n >> 1;
        }
        return true;
    }

}
