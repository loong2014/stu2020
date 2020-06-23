package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 删除排序数组中的重复项
 */
public class SuanFaSolution10 {


    public static void main(String[] args) {

        int[] nums = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        Object out = removeDuplicates(nums);

        System.out.println("out :" + out);
    }

    /**
     * 一定要认真审题，这里只是让输出新数组的有效长度，然后通过长度对旧数组进行处理
     * 并没有要求对数组做删除操作
     */
    private static int removeDuplicates(int[] nums) {

        int outIndex = 0;
        for (int i = 1; i < nums.length; i++) {

            if (nums[outIndex] != nums[i]) {
                outIndex++;
                nums[outIndex] = nums[i];
            }
        }

        return outIndex + 1;
    }

}
