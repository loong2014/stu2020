package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 69. x 的平方根
 * 计算并返回 x 的平方根，其中 x 是非负整数。
 * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
 */
public class SuanFaSolution69 {


    public static void main(String[] args) {
        System.out.println("out :" + mySqrt(2147395599));
    }


    public static int mySqrt(int x) {

        int ans = -1;

        int l = 0;
        int r = x;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if ((long) mid * mid <= x) {
                ans = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        return ans;
    }
}
