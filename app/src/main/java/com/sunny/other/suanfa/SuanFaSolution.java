package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 88. 合并两个有序数组
 * 给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
 *
 * 说明:
 * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
 * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素
 *
 */
public class SuanFaSolution {


    public static void main(String[] args) {
        ListNode node = new ListNode(9);
        System.out.println("out :" + deleteDuplicates(node));
    }


    public static ListNode deleteDuplicates(ListNode head) {

        return null;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
