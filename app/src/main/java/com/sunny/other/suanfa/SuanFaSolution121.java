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
        int[] list = new int[]{2, 5, 1, 3};
        System.out.println("out :" + maxProfit(list));
    }

    /**
     * 动态规划
     */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int len = prices.length;

        /*
            第一维数组：当前天数
            第二维数组：两个状态，'买入'下标为1 和 '将当前股票卖出'下标为0
            二维数组的值表示：两个状态下，用户当天能够获得的最大利润。
        */
        int dp[][] = new int[len][2];

        // 第一天不能进行卖出股票操作 设置为0
        dp[0][0] = 0;

        // 第二天可以进行买入股票，因为买入股票是花钱操作，利润为负数。
        dp[0][1] = -prices[0];

        for (int i = 1; i < len; i++) {
            // 昨天卖出的利润 和 今天卖出的利润(昨天买入花费的最大利润+今天的股票价格 = 今天卖出的利润)取最大
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);

            // 昨天买入的利润 和 今天买入的利润 取最大
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
        }

        return dp[len - 1][0];
    }

    /**
     * 暴力优化版
     */
    public static int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int min = prices[0];
        int maxDif = 0;

        for (int price : prices) {
            min = Math.min(min, price);
            maxDif = Math.max(maxDif, price - min);
        }

        return maxDif;
    }

    /**
     * 暴力
     */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int maxDif = 0;

        for (int i = 0; i < prices.length; i++) {

            for (int j = i + 1; j < prices.length; j++) {
                maxDif = Math.max(prices[j] - prices[i], maxDif);
            }
        }

        return maxDif;
    }
}
