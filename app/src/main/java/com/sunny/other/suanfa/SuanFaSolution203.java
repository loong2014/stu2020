package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

/**
 * 203. 移除链表元素
 * 删除链表中等于给定值 val 的所有节点。
 */
public class SuanFaSolution203 {

    public static void main(String[] args) {

        ListNode node = new ListNode(19);
        System.out.println("out :" + removeElements(node, 7));
    }


    /**
     * 利用哨兵节点处理头部节点
     */
    public static ListNode removeElements(ListNode head, int val) {

        ListNode sentinel = new ListNode(0);
        sentinel.next = head;

        ListNode p = sentinel;

        // 删除其它符合要求的节点
        while (p.next != null) {
            if (p.next.val == val) {
                p.next = p.next.next;
            } else {
                p = p.next;
            }
        }

        return sentinel.next;
    }


    /**
     * 先确认头部节点——循环
     */
    public static ListNode removeElements2(ListNode head, int val) {

        // 确认头节点
        while (head != null) {
            if (head.val == val) {
                head = head.next;
            } else {
                break;
            }
        }
        if (head == null) {
            return null;
        }
        ListNode p = head;

        // 删除其它符合要求的节点
        while (p.next != null) {
            if (p.next.val == val) {
                p.next = p.next.next;
            } else {
                p = p.next;
            }
        }

        return head;
    }


}
