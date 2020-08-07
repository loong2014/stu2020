package com.sunny.other.suanfa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 217. 存在重复元素
 * 给定一个整数数组，判断是否存在重复元素。
 * <p>
 * 如果任意一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。
 */
public class SuanFaSolution217 {

    public static void main(String[] args) {
        int[] list = new int[]{1, 2, 3, 4, 3, 2, 1, 2, 3, 1};
        System.out.println("out :" + containsDuplicate(list));
    }

    /**
     * 排序,相邻且相同
     */
    public static boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);

        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] == nums[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 哈希
     */
    public static boolean containsDuplicate2(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (Integer i : nums) {
            if (!set.add(i)) {
                return true;
            }
            set.add(i);
        }
        return false;
    }

}
