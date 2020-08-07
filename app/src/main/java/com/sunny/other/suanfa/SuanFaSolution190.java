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
     * 取模求和
     * &：只有1+1=1
     * |：一个为1则为1
     * >>：右移一位
     * <<：左移一位
     */
    public static int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res = (res << 1) + (n & 1);
            n >>= 1;
        }
        return res;
    }

    /**
     * 分治合并
     * >>> ：无符号右移，移动后前面补0
     * >> ：带符号移动
     * 如 -12 的二进制为：1111  1111  1111  1111  1111  1111  1111  0100；
     * -12 >> 3 即带符号右移3位，结果是：1111  1111  1111  1111  1111  1111  1111  1110，十进制为： -2；
     * -12 >>> 3 就是右移三位，前面补零，为：0001  1111  1111  1111  1111  1111  1111  1110，十进制为：536870910。
     */
    public static int reverseBits5(int n) {
        n = (n >>> 16) | (n << 16);
        n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >>> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >>> 1) | ((n & 0x55555555) << 1);
        return n;
    }

    /**
     * 按位翻转
     */
    public static int reverseBits4(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {

            res ^= (n & (1 << i)) != 0 ? 1 << (31 - i) : 0;

        }
        return res;
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
