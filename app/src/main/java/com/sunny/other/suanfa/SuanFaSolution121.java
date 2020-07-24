package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 121. 买卖股票的最佳时机
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * 如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
 * 注意：你不能在买入股票前卖出股票。
 */
public class SuanFaSolution121 {

    public static void main(String[] args) {
        int[] list = new int[]{1, 2, 3, 4, 5, 6};
        System.out.println("out :" + maxProfit(list));
    }

    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int buyIndex = 0;
        int sellIndex = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < prices[buyIndex]) {
                buyIndex = i;
                continue;
            }

            if (prices[i] > prices[sellIndex]) {
                sellIndex = i;
                continue;
            }
        }



        return prices[sellIndex] - prices[buyIndex];
    }
}
