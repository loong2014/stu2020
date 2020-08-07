package com.sunny.other.suanfa;

import java.util.Arrays;

/**
 * 204. 计数质数
 * 统计所有小于非负整数 n 的质数的数量。
 * <p>
 * 质数==素数，是指在大于1的自然数中，除了1和它本身以外不再有其他因数的自然数
 */
public class SuanFaSolution204 {

    public static void main(String[] args) {
        System.out.println("out :" + countPrimes(10));
    }


    public static int countPrimes(int n) {

        boolean[] isPrim = new boolean[n];
        Arrays.fill(isPrim, true);

        // 1. i < n 改为 i*i < n
        for (int i = 2; i * i < n; i++) {

            if (isPrim[i]) {
                // 2. j = 2 * i 改为 j = i * i
                for (int j = i * i; j < n; j += i) {
                    isPrim[j] = false;
                }
            }
        }

        // 计算个数
        int count = 0;

        for (int i = 2; i < n; i++) {
            if (isPrim[i]) {
                count++;
            }
        }

        return count;
    }

    /**
     * 常规算法
     */
    public static int countPrimes2(int n) {
        if (n < 2) {
            return 0;
        }

        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }

        return count;
    }

    /**
     * 判断某个数是否为质数
     * 大于 sqrt(n)的范围一定不会出现可整除因子
     */
    private static boolean isPrime2(int n) {

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断某个数是否为质数
     */
    private static boolean isPrime(int n) {

        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}
