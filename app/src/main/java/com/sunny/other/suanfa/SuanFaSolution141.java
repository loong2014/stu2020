package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangxin17 on 2020-04-24
 * 141. 环形链表
 * 给定一个链表，判断链表中是否有环。
 * <p>
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
 * 如果 pos 是 -1，则在该链表中没有环。
 */
public class SuanFaSolution141 {

    public static void main(String[] args) {
        ListNode node = new ListNode(10);
        System.out.println("out :" + hasCycle(node));
    }

    /**
     * 双指针特性
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }

    /**
     * 利用hashSet的特性
     */
    public static boolean hasCycle2(ListNode head) {
        Set<ListNode> set = new HashSet<>();

        while (head != null) {
            if (set.contains(head)) {
                return true;
            } else {
                set.add(head);
            }
            head = head.next;

        }
        return false;
    }
}
