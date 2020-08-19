package com.sunny.other.suanfa;

/**
 * 现在有一个城市销售经理，需要从公司出发，去拜访市内的商家，
 * 已知他的位置以及商家的位置，但是由于城市道路交通的原因，
 * 他只能在左右中选择一个方向，在上下中选择一个方向，现在问他有多少种方案到达商家地址。
 * <p>
 * 给定一个地图map及它的长宽n和m，其中1代表经理位置，2代表商家位置，-1代表不能经过的地区，0代表可以经过的地区，
 * 请返回方案数，保证一定存在合法路径。保证矩阵的长宽都小于等于10。
 */
public class SuanFaSolutionMT10 {

    public static void main(String[] args) {
        int[] list = new int[]{2, 10, 1, 7};

        System.out.println("out :" + maxProfit(list, 4));
    }


    public static int maxProfit(int[] prices, int n) {
        // write code here
        if (n < 2) {
            return 0;
        }
        int left = 0;
        int right = n;
        int mid = left + 2;

        int max = 0;
        while (mid <= n - 2) {
            int cur = maxProfit(prices, left, mid) + maxProfit(prices, mid, right);
            max = Math.max(max, cur);
            mid++;
        }
        return max;
    }

    public static int maxProfit(int[] prices, int left, int right) {
        // write code here
        int min = prices[left];
        int max = 0;
        for (int i = left; i < right; i++) {
            min = Math.min(min, prices[i]);
            max = Math.max(max, prices[i] - min);
        }
        return max;
    }
}
