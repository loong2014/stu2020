package com.sunny.other.suanfa;

import java.util.HashMap;
import java.util.Map;

/**
 * 167. 两数之和 II - 输入有序数组
 * 给定一个已按照升序排列 的有序数组，找到两个数使得它们相加之和等于目标数。
 * <p>
 * 函数应该返回这两个下标值 index1 和 index2，其中 index1 必须小于 index2。
 * <p>
 * 说明:
 * <p>
 * 返回的下标值（index1 和 index2）不是从零开始的。
 * 你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
 */
public class SuanFaSolution167 {

    public static void main(String[] args) {
        int[] nums = new int[]{2, 7, 11, 15};
        System.out.println("out :" + twoSum(nums, 9));
    }


    /**
     * map
     */
    public static int[] twoSum(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2) {
            return null;
        }

        Map<Integer, Integer> map = new HashMap<>(numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            if (map.get(target - numbers[i]) != null) {
                return new int[]{map.get(target - numbers[i]) + 1, i + 1};
            }
            map.put(numbers[i], i);
        }
        return null;
    }


    /**
     * 双指针，缩小查找范围
     */
    public static int[] twoSum4(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2) {
            return null;
        }

        int low = 0;
        int high = numbers.length - 1;
        while (low < high) {
            if (numbers[low] + numbers[high] == target) {
                return new int[]{low + 1, high + 1};
            }

            if (numbers[low] + numbers[high] < target) {
                low++;
            } else {
                high--;
            }
        }

        return null;
    }

    /**
     * 确定一个数后，使用二分查找
     */
    public static int[] twoSum3(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2) {
            return null;
        }

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > target) {
                break;
            }
            // 二分查找
            int left = i + 1;
            int right = numbers.length - 1;
            int num2 = target - numbers[i];

            while (left <= right) {
                int mid = (right - left) / 2 + left;
                if (numbers[mid] == num2) {
                    return new int[]{i + 1, mid + 1};
                }

                if (numbers[mid] > num2) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        return null;
    }


    /**
     * 暴力
     */
    public static int[] twoSum2(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2) {
            return null;
        }

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > target) {
                break;
            }

            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[]{i + 1, j + 1};
                }
                if (numbers[i] + numbers[j] > target) {
                    break;
                }
            }
        }

        return null;
    }
}
