package com.sunny.other.suanfa;

import com.sunny.other.SuanFaTools;

/**
 * 189. 旋转数组
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 */
public class SuanFaSolution189 {

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotate(nums, 3);
        System.out.println("out :" + SuanFaTools.toString(nums));
    }

    /**
     * 暴力
     */
    public static void rotate(int[] nums, int k) {
        if (nums == null || nums.length == 1) {
            return;
        }

        k = k % nums.length;

        int tmp;

        for (int i = 0; i < k; i++) {
            tmp = nums[nums.length - 1];
            for (int j = nums.length - 1; j > 0; j--) {
                nums[j] = nums[j - 1];
            }
            nums[0] = tmp;
        }
    }


}
