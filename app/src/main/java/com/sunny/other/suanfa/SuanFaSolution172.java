package com.sunny.other.suanfa;

/**
 * 172. 阶乘后的零
 * 给定一个整数 n，返回 n! 结果尾数中零的数量。
 */
public class SuanFaSolution172 {

    public static void main(String[] args) {
        System.out.println("out :" + trailingZeroes(30));
    }

    /**
     * 只有五的阶乘（1*2*3*4*5）才会出现一次10
     */
    public static int trailingZeroes(int n) {
        int count = 0;
        while (n > 0) {
            count += n / 5;
            n = n / 5;
        }
        return count;
    }

    /**
     * 超时
     */
    public static int trailingZeroes3(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            int num = i;
            while (num > 0) {
                if (num % 5 == 0) {
                    count++;
                    num = num / 5;
                } else {
                    break;
                }
            }
        }
        return count;
    }

    /**
     * 超时
     */
    public static int trailingZeroes2(int n) {
        int count = 0;
        for (int i = 5; i <= n; i += 5) {
            int tmp = 5;
            while (i % tmp == 0) {
                count++;
                tmp *= 5;
            }
        }
        return count;
    }


}
