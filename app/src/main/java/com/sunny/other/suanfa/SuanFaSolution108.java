package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.TreeNode;

/**
 * Created by zhangxin17 on 2020-04-24
 * 108. 将有序数组转换为二叉搜索树
 */
public class SuanFaSolution108 {


    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
        System.out.println("out :" + sortedArrayToBST(nums));
    }

    public static TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null) {
            return null;
        }
        return helper(nums, 0, nums.length - 1);
    }

    private static TreeNode helper(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }
        int mid = (left + right) / 2;

        TreeNode root = new TreeNode(nums[mid]);
        root.left = helper(nums, left, mid - 1);
        root.right = helper(nums, mid + 1, right);
        return root;
    }

}
