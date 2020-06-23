package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 70. 爬楼梯
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 */
public class SuanFaSolution70 {


    public static void main(String[] args) {
        System.out.println("out :" + climbStairs(1));
    }


    /**
     * f(x) = f(x-1)+f(x-2)
     */
    public static int climbStairs(int n) {
        int ans = 1;
        int p1 = 0;
        int p2 = 1;
        for (int i = 1; i <= n; i++) {
            p2 = p1;
            p1 = ans;
            ans = p1 + p2;
        }

        return ans;
    }


    public static int climbStairs3(int n) {
        int p1 = 0;
        int p2 = 0;
        int ans = 1;

        for (int i = 1; i <= n; i++) {
            p1 = p2;
            p2 = ans;
            ans = p1 + p2;
        }
        return ans;
    }

    public static int climbStairs2(int x) {
        if (x == 0 || x == 1) {
            return 1;
        }
        return climbStairs(x - 1) + climbStairs(x - 2);
    }
}
