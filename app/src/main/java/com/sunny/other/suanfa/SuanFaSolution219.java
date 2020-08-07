package com.sunny.other.suanfa;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 217. 存在重复元素 II
 * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，使得 nums [i] = nums [j]，并且 i 和 j 的差的 绝对值 至多为 k。
 */
public class SuanFaSolution219 {

    public static void main(String[] args) {
        int[] list = new int[]{1, 2, 3, 1};
        System.out.println("out :" + containsNearbyDuplicate(list, 3));
    }

    /**
     * 维护k大小的窗口，散列表——HashSet
     */
    public static boolean containsNearbyDuplicate(int[] nums, int k) {

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                return true;
            }

            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }


    /**
     * 维护k大小的窗口，二叉树——TreeSet
     */
    public static boolean containsNearbyDuplicate3(int[] nums, int k) {

        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                return true;
            }

            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }

    /**
     * 暴力
     */
    public static boolean containsNearbyDuplicate2(int[] nums, int k) {
        for (int i = 0; i < nums.length; i++) {
            int len = Math.min(nums.length, i + k + 1);
            for (int j = i + 1; j < len; j++) {
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }
        return false;
    }

}
