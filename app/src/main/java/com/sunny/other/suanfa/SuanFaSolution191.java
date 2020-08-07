package com.sunny.other.suanfa;

/**
 * 191. 位1的个数
 * 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 ‘1’ 的个数（也被称为汉明重量）。
 */
public class SuanFaSolution191 {

    public static void main(String[] args) {
        System.out.println("out :" + hammingWeight(128));
    }

    public static int hammingWeight(int n) {
        int count = 0;

        for (int i = 0; i < 32; i++) {
            if ((n & 1) == 1) {
                count++;
            }
            n >>= 1;
        }

        return count;
    }

    public static int hammingWeight3(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n &= (n - 1);
        }

        return count;
    }

    /**
     * 循环位移
     */
    public static int hammingWeight2(int n) {
        int count = 0;
        int mask = 1;
        for (int i = 0; i < 32; i++) {
            if ((n & mask) != 0) {
                count++;
            }
            mask <<= 1;
        }

        return count;
    }


}
