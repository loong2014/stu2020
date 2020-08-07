package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

/**
 * 206. 反转链表
 * 反转一个单链表。
 */
public class SuanFaSolution206 {

    public static void main(String[] args) {
        ListNode head = new ListNode(10);
        System.out.println("out :" + reverseList(head));
    }

    /**
     * 递归
     */
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode p = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }


    /**
     * 迭代
     */
    public static ListNode reverseList3(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    /**
     * 迭代
     */
    public static ListNode reverseList2(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode sentinel = new ListNode(0);
        sentinel.next = head;

        ListNode pNode = head.next;
        while (pNode != null) {
            ListNode tmp = pNode;
            pNode = pNode.next;

            tmp.next = sentinel.next;
            sentinel.next = tmp;

        }
        head.next = null;
        return sentinel.next;
    }

}
