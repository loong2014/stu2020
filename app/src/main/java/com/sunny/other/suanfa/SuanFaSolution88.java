package com.sunny.other.suanfa;

import com.sunny.other.SuanFaTools;

/**
 * Created by zhangxin17 on 2020-04-24
 * 88. 合并两个有序数组
 * 给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
 * <p>
 * 说明:
 * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
 * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素
 */
public class SuanFaSolution88 {


    public static void main(String[] args) {
        int[] nums1 = { 0};
        int[] nums2 = {1};
        merge(nums1, 0, nums2, 1);

        System.out.println("out :" + SuanFaTools.toString(nums1));
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {

        int len = m + n;

        int curIndex = len - 1;
        int index1 = m - 1;
        int index2 = n - 1;

        while (curIndex > 0) {
            if (index1 >= 0 && nums1[index1] >= nums2[index2]) {
                nums1[curIndex] = nums1[index1];
                index1--;
            } else {
                nums1[curIndex] = nums2[index2];
                index2--;
            }
            curIndex--;
        }

    }

    public static void merge2(int[] nums1, int m, int[] nums2, int n) {

        int len = m + n;
        int[] out = new int[len];

        int index1 = 0;
        int index2 = 0;
        int outIndex = -1;
        while (++outIndex < len) {

            if (index1 >= m) {
                out[outIndex] = nums2[index2];
                index2++;
                continue;
            }

            if (index2 >= n) {
                out[outIndex] = nums1[index1];
                index1++;
                continue;
            }

            if (nums1[index1] <= nums2[index2]) {
                out[outIndex] = nums1[index1];
                index1++;
            } else {
                out[outIndex] = nums2[index2];
                index2++;
            }
        }

        System.arraycopy(out, 0, nums1, 0, len);
    }
}
