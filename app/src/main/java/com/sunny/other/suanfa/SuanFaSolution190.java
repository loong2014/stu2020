package com.sunny.other.suanfa;

/**
 * 190. 颠倒二进制位
 * 颠倒给定的 32 位无符号整数的二进制位。
 */
public class SuanFaSolution190 {

    public static void main(String[] args) {
        System.out.println("out :" + reverseBits(43261596));
    }


    /**
     * 使用已经实现的方法
     */
    public static int reverseBits(int n) {
        if (n == 0) {
            return 0;
        }
        // 1 将整数转换为二进制
        String binaryString = Integer.toBinaryString(n);

        // 2 二进制反转
        StringBuilder sb = new StringBuilder(binaryString);
        // 2.1 反转
        sb.reverse();

        // 2.2 不足32位，后面补0
        for (int i = binaryString.length(); i < 32; i++) {
            sb.append("0");
        }

        // 3 二进制转换为整数
        return Integer.parseInt(sb.toString(), 2);
    }


    /**
     * 使用已经实现的方法
     */
    public static int reverseBits2(int n) {
        if (n == 0) {
            return 0;
        }
        // 1 将整数转换为二进制
        String binaryString = Integer.toBinaryString(n);

        // 2 二进制反转
        StringBuilder sb = new StringBuilder(binaryString);
        // 2.1 反转
        sb.reverse();

        // 2.2 不足32位，后面补0
        for (int i = binaryString.length(); i < 32; i++) {
            sb.append("0");
        }

        // 3 二进制转换为整数
        return Integer.parseInt(sb.toString(), 2);
    }

}
