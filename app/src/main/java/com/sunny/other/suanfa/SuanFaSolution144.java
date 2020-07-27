package com.sunny.other.suanfa;

import com.sunny.other.suanfa.bean.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangxin17 on 2020-04-24
 * 155. 最小栈
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 *
 * push(x) —— 将元素 x 推入栈中。
 * pop() —— 删除栈顶的元素。
 * top() —— 获取栈顶元素。
 * getMin() —— 检索栈中的最小元素。
 */
public class SuanFaSolution144 {

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
