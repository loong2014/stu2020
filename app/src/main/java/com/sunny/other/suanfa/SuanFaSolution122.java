package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 122. 买卖股票的最佳时机 II
 * <p>
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 */
public class SuanFaSolution122 {

    public static void main(String[] args) {
        int[] list = new int[]{2, 5, 1, 3};
        System.out.println("out :" + maxProfit(list));
    }

    /**
     * 谷峰法，简化
     */
    public static int maxProfit(int[] prices) {
        if (prices == null) {
            return 0;
        }

        int max = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                max += prices[i] - prices[i - 1];
            }
        }
        return max;
    }


    /**
     * 峰谷法
     */
    public static int maxProfit3(int[] prices) {
        if (prices == null) {
            return 0;
        }

        int lowPrice;
        int highPrice;
        int max = 0;

        int i = 0;
        while (i < prices.length - 1) {
            // 下降
            while (i < prices.length - 1 && prices[i + 1] <= prices[i]) {
                i++;
            }
            //底点
            lowPrice = prices[i];

            // 上升
            while (i < prices.length - 1 && prices[i + 1] >= prices[i]) {
                i++;
            }
            // 高点
            highPrice = prices[i];

            max += highPrice - lowPrice;
        }
        return max;
    }

    /**
     * 峰谷法
     */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int lowPrice;
        int highPrice;
        boolean isUp;

        if (prices[1] >= prices[0]) {
            isUp = true;
            highPrice = prices[1];
            lowPrice = prices[0];
        } else {
            isUp = false;
            lowPrice = prices[1];
            highPrice = prices[0];
        }

        int max = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] >= prices[i - 1]) {
                highPrice = prices[i];
                isUp = true;

            } else {
                // 上一个交易日是峰值，卖出
                if (isUp) {
                    max = max + (highPrice - lowPrice);
                }
                lowPrice = prices[i];
                isUp = false;
            }
        }
        // 最后一个交易日，如果是涨，则卖出。
        if (isUp) {
            max = max + (highPrice - lowPrice);
        }

        return max;
    }
}
