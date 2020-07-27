package com.sunny.other.suanfa;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangxin17 on 2020-04-24
 * 136. 只出现一次的数字
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * 说明：
 * 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 */
public class SuanFaSolution136 {

    public static void main(String[] args) {
        int[] nums = new int[]{4, 1, 2, 1, 2};
        System.out.println("out :" + singleNumber(nums));
    }

    /**
     * 哈希
     */
    public static int singleNumber(int[] nums) {

        Set<Integer> set = new HashSet<>();
        for (int i : nums) {
            // set中已经包含了i
            if (!set.add(i)) {
                set.remove(i);
            }
        }
        return set.iterator().next();
    }

    /**
     * 位运算——异或
     */
    public static int singleNumber2(int[] nums) {
        int out = 0;
        for (int i : nums) {
            out = out ^ i;
        }
        return out;
    }
}
