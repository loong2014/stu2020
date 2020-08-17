package com.sunny.other.suanfa;

/**
 * 268. 缺失数字
 * <p>
 * 给定一个包含 0, 1, 2, ..., n 中 n 个数的序列，找出 0 .. n 中没有出现在序列中的那个数。
 * <p>
 * 你的算法应具有线性时间复杂度。你能否仅使用额外常数空间来实现?
 */
public class SuanFaSolution268 {

    public static void main(String[] args) {
//        int[] nums = new int[]{0,1, 2, 3, 4, 6, 7, 8};
        int[] nums = new int[]{3, 0, 1};
        System.out.println("out :" + missingNumber(nums));
    }

    /**
     * 位运算，对一个数进行两次异或运算会得到原来的数
     * 我们知道数组中有 nn 个数，并且缺失的数在 [0..n][0..n] 中。
     * 因此我们可以先得到 [0..n][0..n] 的异或值，再将结果对数组中的每一个数进行一次异或运算。
     * 未缺失的数在 [0..n][0..n] 和数组中各出现一次，因此异或后得到 0。
     * 而缺失的数字只在 [0..n][0..n] 中出现了一次，在数组中没有出现，因此最终的异或结果即为这个缺失的数字。
     */
    public static int missingNumber(int[] nums) {
        int missNum = nums.length;
        for (int i = 0; i < nums.length; i++) {
            missNum ^= i ^ nums[i];
        }
        return missNum;
    }

    /**
     * 高斯求和
     */
    public static int missingNumber3(int[] nums) {

        int mustSum = nums.length * (nums.length + 1) / 2;
        int curSum = 0;
        for (int num : nums) {
            curSum += num;
        }

        return mustSum - curSum;
    }

    public static int missingNumber2(int[] nums) {

        int mustSum = 0;
        int curSum = 0;
        int i;
        for (i = 0; i < nums.length; i++) {

            curSum += nums[i];
            mustSum += i;
        }
        mustSum += i;

        return mustSum - curSum;
    }
}
