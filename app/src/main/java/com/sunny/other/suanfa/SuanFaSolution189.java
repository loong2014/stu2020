package com.sunny.other.suanfa;

import com.sunny.other.SuanFaTools;

/**
 * 189. 旋转数组
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 */
public class SuanFaSolution189 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6};
        rotate(nums, 4);
        System.out.println("out :" + SuanFaTools.toString(nums));
    }


    /**
     * 环状替换
     */
    public static void rotate(int[] nums, int k) {
        if (nums == null || nums.length == 1) {
            return;
        }
        k = k % nums.length;

        int count = 0;
        for (int start = 0; count < nums.length; start++) {
            int current = start;
            int prev = nums[start];
            System.out.println("111 start=" + start + " : " + SuanFaTools.toString(nums));

            do {
                int next = (current + k) % nums.length;
                int temp = nums[next];
                nums[next] = prev;
                prev = temp;

                current = next;
                count++;
            } while (start != current);
            System.out.println("222 start=" + start + " : " + SuanFaTools.toString(nums));
        }
    }

    /**
     * 反转
     */
    public static void rotate4(int[] nums, int k) {
        if (nums == null || nums.length == 1) {
            return;
        }
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * 暴力
     */
    public static void rotate3(int[] nums, int k) {
        if (nums == null || nums.length == 1) {
            return;
        }

        k = k % nums.length;

        for (int i = 0; i < k; i++) {

            int j = nums.length - 1;
            int tmp = nums[j];
            for (; j > 0; j--) {
                nums[j] = nums[j - 1];
            }
            nums[0] = tmp;
        }
    }


    /**
     * 额外数组
     */
    public static void rotate22(int[] nums, int k) {
        if (nums == null || nums.length == 1) {
            return;
        }

        int len = nums.length;
        k = k % len;

        int[] outNums = new int[nums.length];

        for (int i = 0; i < len; i++) {
            outNums[(i + k) % len] = nums[i];
        }

        for (int i = 0; i < len; i++) {
            nums[i] = outNums[i];
        }
    }

    /**
     * 额外数组
     */
    public static void rotate2(int[] nums, int k) {
        if (nums == null || nums.length == 1) {
            return;
        }

        int len = nums.length;
        k = k % len;

        int[] outNums = new int[nums.length];
        int index = 0;
        for (int i = len - k; i < nums.length; i++, index++) {
            outNums[index] = nums[i];
        }

        for (int i = 0; i < len - k; i++, index++) {
            outNums[index] = nums[i];
        }

        for (int i = 0; i < len; i++) {
            nums[i] = outNums[i];
        }
    }

}
