package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 234. 回文链表
 * 请判断一个链表是否为回文链表。
 */
public class SuanFaSolution234 {

    public static void main(String[] args) {
        ListNode head = new ListNode(-129);
        ListNode next = new ListNode(-129);
        head.next = next;
        System.out.println("out :" + isPalindrome(head));
    }

    private static ListNode frontP;

    /**
     * 快慢指针，找中间节点
     * 反转链表，逐一比较
     */
    public static boolean isPalindrome(ListNode head) {

        if (head == null) {
            return true;
        }

        ListNode firstHalfEnd = endOfFirstHalf(head);
        ListNode secondHalfStart = reverseList(firstHalfEnd.next);

        // 比较
        ListNode p1 = head;
        ListNode p2 = secondHalfStart;
        while (p2 != null) {
            if (p1.val != p2.val) {
                return false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }

        return true;
    }

    /**
     * 链表反转
     */
    private static ListNode reverseList(ListNode head) {
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
     * 链表中间节点
     */
    private static ListNode endOfFirstHalf(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }


    /**
     * 递归，利用递归的反向迭代
     */
    public static boolean isPalindrome4(ListNode head) {
        frontP = head;
        return recursivelyCheck(head);
    }

    private static boolean recursivelyCheck(ListNode curNode) {
        if (curNode != null) {
            if (!recursivelyCheck(curNode.next)) {
                return false;
            }
            if (curNode.val != frontP.val) {
                return false;
            }
            frontP = frontP.next;
        }
        return true;
    }


    /**
     * 借助数组
     */
    public static boolean isPalindrome3(ListNode head) {
        List<Integer> list = new ArrayList<>();

        while (head != null) {
            list.add(head.val);
            head = head.next;
        }

        int len = list.size();
        if (len < 2) {
            return true;
        }

        int pF = 0;
        int pL = len - 1;
        while (pF < pL) {
            if (!list.get(pF).equals(list.get(pL))) {
                return false;
            }
            pF++;
            pL--;
        }
        return true;
    }

    /**
     * 借助stack
     */
    static boolean isPalindrome2(ListNode head) {
        Stack<Integer> stack = new Stack<>();

        int len = 0;
        ListNode p = head;
        while (p != null) {
            stack.push(p.val);
            p = p.next;
            len++;
        }

        len = len / 2;
        p = head;
        while (p != null && len > 0) {
            if (stack.pop() != p.val) {
                return false;
            }
            p = p.next;
            len--;
        }

        return true;
    }
}
