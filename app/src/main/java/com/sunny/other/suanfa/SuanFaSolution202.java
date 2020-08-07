package com.sunny.other.suanfa;

import java.util.HashSet;
import java.util.Set;

/**
 * 202. 快乐数
 * 编写一个算法来判断一个数 n 是不是快乐数。
 * <p>
 * 「快乐数」定义为：对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和，然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。如果 可以变为  1，那么这个数就是快乐数。
 * <p>
 * 如果 n 是快乐数就返回 True ；不是，则返回 False 。
 *
 * 主要问题是解决无限循环问题
 */
public class SuanFaSolution202 {

    public static void main(String[] args) {

        System.out.println("out :" + isHappy(19));
    }

    /**
     * 快慢指针
     */
    public static boolean isHappy(int n) {
        if (n <= 0) {
            return false;
        }

        long slowRunner = n;
        long fastRunner = getNext(n);

        while (fastRunner != 1 && slowRunner != fastRunner) {

            slowRunner = getNext(slowRunner);
            fastRunner = getNext(getNext(fastRunner));
        }
        return fastRunner==1;
    }

    /**
     * 通过哈希表
     */
    public static boolean isHappy2(int n) {
        if (n <= 0) {
            return false;
        }

        Set<Long> set = new HashSet<>();

        long tmp = getNext(n);
        while (tmp != 1 && !set.contains(tmp)) {
            set.add(tmp);
            tmp = getNext(tmp);
        }
        return true;
    }

    private static long getNext(long n) {
        long tmp = 0;
        while (n > 0) {
            long a = n % 10;
            n = n / 10;

            tmp += a * a;
        }

        return tmp;
    }


}
