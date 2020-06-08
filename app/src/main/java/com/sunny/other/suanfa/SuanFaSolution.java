package com.sunny.other.suanfa;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxin17 on 2020-04-24
 */
public class SuanFaSolution {


    public static void main(String[] args) {

        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] out = twoSum(nums, target);

        System.out.println("[" + out[0] + " , " + out[1] + "]");


    }

    /**
     * 时间：O(n)
     * 空间：O(n)
     */
    private static int[] twoSum(int[] nums, int target) {
        int[] out = new int[2];
        boolean has = false;

        int len = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < len; i++) {
            int m = target - nums[i];
            if (map.containsKey(m) && map.get(m) != i) {
                out = new int[]{i, map.get(m)};
                has = true;
                break;
            }
        }
        return out;
    }


    /**
     * 时间：O(n^2)
     * 空间：O(1)
     */
    private static int[] twoSum2(int[] nums, int target) {
        int[] out = new int[2];

        int len = nums.length;
        int i = -1;
        int j = -1;
        boolean has = false;
        for1:
        for (i = 0; i < len; i++) {

            for (j = i + 1; j < len; j++) {
                System.out.println("[ " + i + " , " + j + " ] = [ " + nums[i] + " + " + nums[j] + "]");

                if (nums[i] + nums[j] == target) {
                    has = true;
                    System.out.println("doExit");
                    break for1;
                }
            }
        }

        if (has) {
            out[0] = i;
            out[1] = j;
        } else {
            out = new int[]{-1, -1};
        }

        return out;
    }

}
