package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 53. 最大子序和
 */
public class SuanFaSolution53 {


    public static void main(String[] args) {

        int[] nums = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4, -5, -4, -9, 2, -1, 8, 5};
//        int[] nums = new int[]{-2, 1};
        Object out = maxSubArray(nums);
        System.out.println("out :" + out);
    }

    /**
     * 动态规划
     * 计算以index为结尾的数组的最大和，然后再计算出整个数组的最大和
     */
    private static int maxSubArray(int[] nums) {
        int pre = 0;
        int max = nums[0];

        for (int x : nums) {

            if (pre <= 0) {
                pre = x;
            } else {
                pre = pre + x;
            }

            if (pre > max) {
                max = pre;
            }
        }

        return max;
    }


    /**
     * 动态规划
     * 计算以index为结尾的数组的最大和，然后再计算出整个数组的最大和
     */
    private static int maxSubArray3(int[] nums) {
        int pre = 0;
        int max = nums[0];

        for (int x : nums) {
            pre = Math.max(pre + x, x);
            max = Math.max(pre, max);
        }

        return max;
    }

    /**
     * 超时
     */
    private static int maxSubArray2(int[] nums) {
        if (nums == null || nums.length <= 0) {
            return 0;
        }

        if (nums.length == 1) {
            return nums[0];
        }


        int max = nums[0];
        int maxLeft = 0;
        int maxRight = 0;

        int left = 0;
        int right = nums.length - 1;

        while (true) {

            int sum = 0;
            for (int i = left; i <= right; i++) {
                sum = sum + nums[i];
            }

            if (sum > max) {
                max = sum;
                maxLeft = left;
                maxRight = right;
            }
            if (right > left) {
                right--;
            } else {
                left++;
                right = nums.length - 1;
            }

            if (left > nums.length - 1) {
                break;
            }
        }

        return max;
    }


}
