package com.sunny.other.suanfa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 169. 多数元素
 * 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数大于 ⌊ n/2 ⌋ 的元素。
 * <p>
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 */
public class SuanFaSolution169 {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 2};
        System.out.println("out :" + majorityElement(nums));
    }


    /**
     * Boyer-Moore 投票算法
     */
    public static int majorityElement(int[] nums) {
        int count = 0;
        Integer candidate = null;
        for (int n : nums) {
            if (count == 0) {
                candidate = n;
            }
            count += (n == candidate) ? 1 : -1;
        }
        return candidate;
    }


    /**
     * 排序，第len/2个元素
     */
    public static int majorityElement3(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    /**
     * map
     */
    public static int majorityElement2(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();

        int maxV = 0;
        for (int i : nums) {
            Integer v = map.get(i);
            if (v == null) {
                v = 1;
            } else {
                v = v + 1;
            }
            map.put(i, v);

            if (v > maxV) {
                if (v > nums.length / 2) {
                    return i;
                }
                maxV = v;
            }
        }
        return -1;
    }
}
