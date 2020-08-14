package com.sunny.other.suanfa;

/**
 * 263. 丑数 II
 * 编写一个程序，找出第 n 个丑数。
 * <p>
 * 丑数就是质因数只包含 2, 3, 5 的正整数。
 * <p>
 * 习惯上我们把1当做是第一个丑数。
 * 1, 2, 3, 4, 5, 6, 8, 9, 10, 12 是前 10 个丑数。
 */
public class SuanFaSolution264 {

    public static void main(String[] args) {
        System.out.println("out :" + nthUglyNumber(11));
    }

    public static int nthUglyNumber(int n) {

        int count = 1;
        int num = 1;

        int index2 = 1;
        int index3 = 1;
        int index5 = 1;

        while (count < n) {
            int next2 = index2 * 2;
            if (!isUgly(next2) || next2 <= num) {
                index2++;
                continue;
            }

            int next3 = index3 * 3;
            if (!isUgly(next3) || next3 <= num) {
                index3++;
                continue;
            }
            int next5 = index5 * 5;
            if (!isUgly(next5) || next5 <= num) {
                index5++;
                continue;
            }

            if (next2 <= next3 && next2 <= next5) {
                index2++;
                num = next2;
                count++;
                continue;
            }

            if (next3 <= next2 && next3 <= next5) {
                index3++;
                num = next3;
                count++;
                continue;
            }

            if (next5 <= next2 && next5 <= next3) {
                index5++;
                num = next5;
                count++;
                continue;
            }
        }

        return num;
    }

    private static boolean isUgly(int num) {
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
