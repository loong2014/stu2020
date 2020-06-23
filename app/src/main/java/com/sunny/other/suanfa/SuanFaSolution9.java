package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 合并两个有序链表
 */
public class SuanFaSolution9 {


    public static void main(String[] args) {

    }

    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        if (l1 ==null){
            return l2;
        }

        if (l2 ==null){
            return l1;
        }

        if (l1.val < l2.val){
            l1.next = mergeTwoLists(l1.next,l2);
            return l1;
        }
        l2.next = mergeTwoLists(l1,l2.next);
        return l2;
    }

    private static void logic(ListNode l1, ListNode l2, ListNode out) {

        if (l1 == null) {
            out.next = l2;
            return;
        }

        if (l2 == null) {
            out.next = l1;
            return;
        }

        if (l1.val <= l2.val) {
            out.next = l1;
            logic(l1.next, l2, l1);
        } else {
            out.next = l2;
            logic(l1, l2.next, l2);
        }

    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
