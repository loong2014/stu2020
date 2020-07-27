package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

/**
 * Created by zhangxin17 on 2020-04-24
 * 83. 删除排序链表中的重复元素
 * 给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。
 */
public class SuanFaSolution83 {


    public static void main(String[] args) {
        ListNode node = new ListNode(9);
        System.out.println("out :" + deleteDuplicates(node));
    }

    public static ListNode deleteDuplicates(ListNode head) {
        ListNode curNode = head;
        while (curNode != null && curNode.next != null) {

            if (curNode.next.val == curNode.val) {
                curNode.next = curNode.next.next;
            } else {
                curNode = curNode.next;
            }
        }
        return head;
    }

    public static ListNode deleteDuplicates2(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode rstNode = head;
        ListNode tmpNode = head.next;
        int curVal = head.val;

        ListNode nextNode;
        while (tmpNode != null) {
            nextNode = tmpNode.next;

            if (tmpNode.val > curVal) {
                head.next = tmpNode;
                curVal = tmpNode.val;
                head = tmpNode;
            }
            tmpNode = nextNode;
        }

        head.next = null;
        return rstNode;
    }

}
